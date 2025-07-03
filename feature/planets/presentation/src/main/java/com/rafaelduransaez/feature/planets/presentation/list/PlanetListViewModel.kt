package com.rafaelduransaez.feature.planets.presentation.list

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.rafaelduransaez.core.base.presentation.SwapiViewModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import com.rafaelduransaez.feature.planets.domain.usecases.GetPlanetListUseCase
import com.rafaelduransaez.feature.planets.domain.usecases.RefreshPlanetsUseCase
import com.rafaelduransaez.feature.planets.presentation.R

import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(
    private val uiMapper: PlanetListUiMapper,
    private val getPlanetListUseCase: GetPlanetListUseCase,
    private val refreshPlanetsUseCase: RefreshPlanetsUseCase
) : SwapiViewModel<PlanetListUiEffect>() {

    private val _uiState = MutableStateFlow(PlanetListUiState())

    val uiState = _uiState
        .map { state ->
            val filteredPlanets = if (state.searchQuery.isBlank()) {
                state.allPlanets
            } else {
                state.allPlanets.filter { planet ->
                    planet.name.contains(state.searchQuery, ignoreCase = true) ||
                    planet.climate.contains(state.searchQuery, ignoreCase = true)
                }
            }
            state.copy(filteredPlanets = filteredPlanets)
        }
        .onStart { loadPlanets() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(CACHE_TIMEOUT),
            initialValue = PlanetListUiState()
        )

    internal fun onUiEvent(event: PlanetListUiEvent) {
        when (event) {
            is PlanetListUiEvent.ShowDetail -> navigateTo(PlanetListUiEffect.NavigateToDetail(event.uid))
            PlanetListUiEvent.Retry -> loadPlanets()
            PlanetListUiEvent.Refresh -> refreshPlanets()
            is PlanetListUiEvent.Search -> updateSearchQuery(event.query)
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun loadPlanets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessageId = null) }
            getPlanetListUseCase().collect {
                it.fold(
                    onSuccess = ::onGetPlanetsSuccess,
                    onFailure = ::onGetPlanetsFailure
                )
            }
        }
    }

    private fun refreshPlanets() {
        viewModelScope.launch {
            refreshPlanetsUseCase().collect {
                it.fold(
                    onSuccess = {
                        // Refresh successful - data will be automatically updated through getPlanets flow
                    },
                    onFailure = {
                        //show snackbar
                    }
                )
            }
        }
    }

    private fun onGetPlanetsSuccess(planets: List<PlanetSummaryModel>) {
        _uiState.update { 
            it.copy(
                isLoading = false, 
                errorMessageId = null, 
                allPlanets = planets
            )
        }
    }

    private fun onGetPlanetsFailure(error: PlanetError) {
        val messageId = uiMapper.getErrorMessageId(error)
        _uiState.update { 
            it.copy(
                isLoading = false, 
                errorMessageId = messageId, 
                allPlanets = emptyList()
            )
        }
    }

    companion object {
        const val CACHE_TIMEOUT = 5000L
    }
}

// Just one data class - no intermediate classes needed!
data class PlanetListUiState(
    val isLoading: Boolean = true,
    val errorMessageId: Int? = null,
    val allPlanets: List<PlanetSummaryModel> = emptyList(),
    val filteredPlanets: List<PlanetSummaryModel> = emptyList(),
    val searchQuery: String = ""
)

sealed interface PlanetListUiEvent {
    data class ShowDetail(val uid: String) : PlanetListUiEvent
    data object Retry : PlanetListUiEvent
    data object Refresh : PlanetListUiEvent
    data class Search(val query: String) : PlanetListUiEvent
}

sealed interface PlanetListUiEffect {
    data class NavigateToDetail(val planetUid: String) : PlanetListUiEffect
}

