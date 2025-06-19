package com.rafaelduransaez.feature.planets.presentation.list


import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.SwapiResult.Success
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.usecases.GetPlanetListUseCase
import com.rafaelduransaez.feature.planets.presentation.SwapiViewModelTest
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiState.Loading
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiState.ShowPlanets
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import com.rafaelduransaez.feature.planets.presentation.mockPlanetSummaryList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class PlanetListViewModelTest :
    SwapiViewModelTest<PlanetListViewModel, PlanetListUiState, PlanetListUiEffect>() {

    private val uiMapper = mock<PlanetListUiMapper>()
    private val getPlanetListUseCase = mock<GetPlanetListUseCase>()

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = PlanetListViewModel(uiMapper, getPlanetListUseCase)
    }

    @Test
    fun `uiState emits Loading then ShowPlanets on success load`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)
        val expectedResult = listOf(Loading, ShowPlanets(mockPlanetSummaryList))

        assertEquals(expectedResult, emittedStates)
    }

    @Test
    fun `uiState emits Loading then Error on failed load`() = runTest {
        val randomMessageId = 1
        whenever(uiMapper.getErrorMessageId(any())).doReturn(randomMessageId)
        whenever(getPlanetListUseCase()).doReturn(flowOf(SwapiResult.Failure(PlanetError.ServerError)))

        collectViewModelState(viewModel.uiState)
        val expectedResult = listOf(Loading, PlanetListUiState.Error(randomMessageId))

        assertEquals(expectedResult, emittedStates)
    }

    @Test
    fun `on clicking a planet it navigates to its detail`() = runTest {
        val detailUid = mockPlanetSummaryList[0].uid
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)
        collectNavState(viewModel)

        viewModel.onUiEvent(PlanetListUiEvent.ShowDetail(detailUid))

        assertEquals(PlanetListUiEffect.NavigateToDetail(detailUid), navStates.last())
    }

    @Test
    fun `on clicking Retry after error it keeps on same screen`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)
        collectNavState(viewModel)

        viewModel.onUiEvent(PlanetListUiEvent.Retry)

        assertEquals(0, navStates.size)
    }
}