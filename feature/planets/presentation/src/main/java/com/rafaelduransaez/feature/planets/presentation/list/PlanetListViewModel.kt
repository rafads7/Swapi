package com.rafaelduransaez.feature.planets.presentation.list

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.rafaelduransaez.core.base.presentation.SwapiViewModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import com.rafaelduransaez.feature.planets.domain.usecases.GetPlanetListUseCase
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiEffect.*
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val uiMapper: PlanetListUiMapper,
    private val getPlanetListUseCase: GetPlanetListUseCase
) : SwapiViewModel<PlanetListUiEffect>() {

    private val _uiState = MutableStateFlow<PlanetListUiState>(PlanetListUiState.Loading)
    val uiState = _uiState
        .onStart { loadPlanets() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(CACHE_TIMEOUT),
            initialValue = PlanetListUiState.Loading
        )

    internal fun onUiEvent(event: PlanetListUiEvent) {
        when (event) {
            is PlanetListUiEvent.ShowDetail -> navigateTo(NavigateToDetail(event.uid))
            PlanetListUiEvent.Retry -> loadPlanets()
        }
    }

    private fun loadPlanets() {
        viewModelScope.launch {
            _uiState.update { PlanetListUiState.Loading }
            getPlanetListUseCase().collect {
                it.fold(
                    onSuccess = ::onGetPlanetsSuccess,
                    onFailure = ::onGetPlanetsFailure
                )
            }
        }
    }

    private fun onGetPlanetsSuccess(planets: List<PlanetSummaryModel>) {
        _uiState.update { PlanetListUiState.ShowPlanets(planets) }
    }

    private fun onGetPlanetsFailure(error: PlanetError) {
        val messageId = uiMapper.getErrorMessageId(error)
        _uiState.update { PlanetListUiState.Error(messageId) }
    }

    companion object {
        const val CACHE_TIMEOUT = 5000L
    }
}

sealed interface PlanetListUiState {
    data object Loading : PlanetListUiState
    data class ShowPlanets(val planets: List<PlanetSummaryModel>) : PlanetListUiState
    data class Error(@StringRes val errorMessageId: Int) : PlanetListUiState
}

sealed interface PlanetListUiEvent {
    data class ShowDetail(val uid: String) : PlanetListUiEvent
    data object Retry : PlanetListUiEvent
}

sealed interface PlanetListUiEffect {
    data class NavigateToDetail(val planetUid: String) : PlanetListUiEffect
}

