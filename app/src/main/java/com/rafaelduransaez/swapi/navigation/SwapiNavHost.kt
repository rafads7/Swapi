package com.rafaelduransaez.swapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetListNavigator
import com.rafaelduransaez.feature.planets.presentation.navigation.navigateToPlanetDetail
import com.rafaelduransaez.feature.planets.presentation.navigation.planetsGraph
import com.rafaelduransaez.swapi.ui.SwapiAppState

@Composable
fun SwapiNavHost(
    swapiAppState: SwapiAppState
) {
    val navController = swapiAppState.navController
    NavHost(
        navController = navController,
        startDestination = PlanetListNavigator,
    ) {
        planetsGraph(
            onPlanetClick = navController::navigateToPlanetDetail,
            onBack = swapiAppState::navigateBack
        )
    }
}