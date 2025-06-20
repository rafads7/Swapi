package com.rafaelduransaez.feature.planets.presentation.detail

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelduransaez.core.base.presentation.SwapiViewModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import com.rafaelduransaez.feature.planets.domain.usecases.GetPlanetDetailUseCase
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailUiEffect.*
import com.rafaelduransaez.feature.planets.presentation.detail.mapper.PlanetDetailUiMapper
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiEffect
import com.rafaelduransaez.feature.planets.presentation.utils.PlanetUid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetDetailViewModel @Inject constructor(
    @PlanetUid private val planetUid: String,
    private val uiMapper: PlanetDetailUiMapper,
    private val getPlanetDetailUseCase: GetPlanetDetailUseCase
) : SwapiViewModel<PlanetDetailUiEffect>() {

    private val _uiState = MutableStateFlow<PlanetDetailUiState>(PlanetDetailUiState.Loading)
    val uiState = _uiState
        .onStart { getDetail(planetUid) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(CACHE_TIMEOUT),
            initialValue = PlanetDetailUiState.Loading
        )

    internal fun onUiEvent(event: PlanetDetailUiEvent) {
        when (event) {
            is PlanetDetailUiEvent.Back -> navigateTo(Back)
            PlanetDetailUiEvent.Retry -> getDetail(planetUid)
        }
    }

    private fun getDetail(planetUid: String) {
        viewModelScope.launch {
            _uiState.update { PlanetDetailUiState.Loading }
            getPlanetDetailUseCase(planetUid).collect {
                it.fold(
                    onSuccess = ::onGetPlanetSuccess,
                    onFailure = ::onGetPlanetFailure
                )
            }
        }
    }

    private fun onGetPlanetSuccess(planet: PlanetDetailModel) {
        _uiState.update { PlanetDetailUiState.Success(planet.name, uiMapper.map(planet)) }

    }

    private fun onGetPlanetFailure(error: PlanetError) {
        val messageId = uiMapper.getErrorMessageId(error)
        _uiState.update { PlanetDetailUiState.Error(messageId) }
    }

    companion object {
        const val CACHE_TIMEOUT = 5000L
    }
}

sealed interface PlanetDetailUiState {
    data object Loading : PlanetDetailUiState
    data class Success(val planetName: String, val planetFeatures: List<PlanetFeature>) : PlanetDetailUiState
    data class Error(@StringRes val errorMessageId: Int) : PlanetDetailUiState
}

sealed interface PlanetDetailUiEvent {
    data object Retry : PlanetDetailUiEvent
    data object Back : PlanetDetailUiEvent
}

sealed interface PlanetDetailUiEffect {
    data object Back : PlanetDetailUiEffect
}