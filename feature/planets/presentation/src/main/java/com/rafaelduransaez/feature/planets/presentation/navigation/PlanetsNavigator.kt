package com.rafaelduransaez.feature.planets.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rafaelduransaez.core.ui.ObserveAsEffect
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailScreen
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailUiEffect
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailViewModel
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListScreen
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiEffect
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListViewModel
import kotlinx.serialization.Serializable

@Serializable
data object PlanetListNavigator

@Serializable
data object PlanetList

@Serializable
data class PlanetDetail(val planetUid: String)

fun NavController.navigateToPlanetDetail(planetUid: String, navOptions: NavOptions? = null) {
    navigate(PlanetDetail(planetUid), navOptions)
}

fun NavGraphBuilder.planetsGraph(
    onPlanetClick: (String) -> Unit,
    onBack: () -> Unit
) {

    val animationSpec = tween<IntOffset>(durationMillis = ANIMATION_DURATION)

    navigation<PlanetListNavigator>(startDestination = PlanetList) {
        composable<PlanetList>(
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = animationSpec
                )
            }
        ) {
            val viewModel = hiltViewModel<PlanetListViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveAsEffect(flow = viewModel.navState, key1 = true) {
                when (it) {
                    is PlanetListUiEffect.NavigateToDetail -> onPlanetClick(it.planetUid)
                }
            }

            PlanetListScreen(uiState = uiState, onUiEvent = viewModel::onUiEvent)
        }

        composable<PlanetDetail>{
            val viewModel = hiltViewModel<PlanetDetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ObserveAsEffect(flow = viewModel.navState, key1 = true) {
                when (it) {
                    PlanetDetailUiEffect.Back -> onBack()
                }
            }

            PlanetDetailScreen(uiState = uiState, onUiEvent = viewModel::onUiEvent)
        }
    }
}

const val ANIMATION_DURATION = 700
