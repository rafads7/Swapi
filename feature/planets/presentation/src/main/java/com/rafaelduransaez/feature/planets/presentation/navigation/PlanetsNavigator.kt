package com.rafaelduransaez.feature.planets.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailScreen
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailUiEffect
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailUiEvent
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetDetailViewModel
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListScreen
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiEffect
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListViewModel
import com.rafaelduransaez.ui.LocalTopAppBarController
import com.rafaelduransaez.ui.ObserveAsEffect
import com.rafaelduransaez.ui.SwapiTopAppBarConfig
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

    val animationSpec = tween<IntOffset>(durationMillis = 700)

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
            val topAppBarController = LocalTopAppBarController.current

            ObserveAsEffect(flow = viewModel.navState, key1 = true) {
                when (it) {
                    is PlanetListUiEffect.NavigateToDetail -> onPlanetClick(it.planetUid)
                }
            }

            LaunchedEffect(Unit) {
                topAppBarController.setTopAppBar(
                    SwapiTopAppBarConfig(titleResId = R.string.planets_title)
                )
            }

            PlanetListScreen(uiState = uiState, onUiEvent = viewModel::onUiEvent)
        }

        composable<PlanetDetail>{
            val viewModel = hiltViewModel<PlanetDetailViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val topAppBarController = LocalTopAppBarController.current

            ObserveAsEffect(flow = viewModel.navState, key1 = true) {
                when (it) {
                    PlanetDetailUiEffect.Back -> onBack()
                }
            }
            LaunchedEffect(Unit) {
                topAppBarController.setTopAppBar(
                    SwapiTopAppBarConfig(
                        titleResId = R.string.planet_detail,
                        navIcon = Icons.AutoMirrored.Default.ArrowBack,
                        onNavIconClicked = { viewModel.onUiEvent(PlanetDetailUiEvent.Back) }
                    )
                )
            }

            PlanetDetailScreen(uiState = uiState, onUiEvent = viewModel::onUiEvent)
        }
    }
}
