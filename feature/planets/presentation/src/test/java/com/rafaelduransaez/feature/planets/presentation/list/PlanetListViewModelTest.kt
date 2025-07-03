package com.rafaelduransaez.feature.planets.presentation.list


import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.SwapiResult.Success
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.usecases.GetPlanetListUseCase
import com.rafaelduransaez.feature.planets.domain.usecases.RefreshPlanetsUseCase
import com.rafaelduransaez.feature.planets.presentation.SwapiViewModelTest
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import com.rafaelduransaez.feature.planets.presentation.mockPlanetSummaryList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    private val refreshPlanetsUseCase = mock<RefreshPlanetsUseCase>()

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = PlanetListViewModel(uiMapper, getPlanetListUseCase, refreshPlanetsUseCase)
    }

    @Test
    fun `uiState starts with loading state`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)
        
        // First state should be loading
        assertTrue(emittedStates.first().isLoading)
    }

    @Test
    fun `uiState shows planets on success load`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)
        
        // Wait for data to load and check final state
        val finalState = emittedStates.last()
        assertEquals(false, finalState.isLoading)
        assertEquals(null, finalState.errorMessageId)
        assertEquals(mockPlanetSummaryList, finalState.allPlanets)
    }

    @Test
    fun `uiState shows error on failed load`() = runTest {
        val randomMessageId = 1
        whenever(uiMapper.getErrorMessageId(any())).doReturn(randomMessageId)
        whenever(getPlanetListUseCase()).doReturn(flowOf(SwapiResult.Failure(PlanetError.ServerError)))

        collectViewModelState(viewModel.uiState)
        
        val finalState = emittedStates.last()
        assertEquals(false, finalState.isLoading)
        assertEquals(randomMessageId, finalState.errorMessageId)
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

    @Test
    fun `search updates searchQuery in uiState and filters planets`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)

        // Perform search
        viewModel.onUiEvent(PlanetListUiEvent.Search("Tatooine"))

        val finalState = emittedStates.last()
        assertEquals("Tatooine", finalState.searchQuery)
        assertEquals(1, finalState.filteredPlanets.size)
        assertEquals("Tatooine", finalState.filteredPlanets.first().name)
    }

    @Test
    fun `search by climate filters planets correctly`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)

        // Search by climate
        viewModel.onUiEvent(PlanetListUiEvent.Search("Arid"))

        val finalState = emittedStates.last()
        assertEquals("Arid", finalState.searchQuery)
        assertTrue(finalState.filteredPlanets.isNotEmpty())
        assertTrue(finalState.filteredPlanets.all { 
            it.climate.contains("Arid", ignoreCase = true) 
        })
    }

    @Test
    fun `empty search query shows all planets`() = runTest {
        whenever(getPlanetListUseCase()).doReturn(flowOf(Success(mockPlanetSummaryList)))

        collectViewModelState(viewModel.uiState)

        // First search for something specific
        viewModel.onUiEvent(PlanetListUiEvent.Search("Tatooine"))
        // Then clear search
        viewModel.onUiEvent(PlanetListUiEvent.Search(""))

        val finalState = emittedStates.last()
        assertEquals("", finalState.searchQuery)
        assertEquals(mockPlanetSummaryList.size, finalState.filteredPlanets.size)
        assertEquals(mockPlanetSummaryList, finalState.filteredPlanets)
    }

}
