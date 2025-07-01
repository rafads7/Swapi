package com.rafaelduransaez.swapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetListNavigator
import com.rafaelduransaez.feature.planets.presentation.navigation.navigateToPlanetDetail
import com.rafaelduransaez.feature.planets.presentation.navigation.planetsGraph
import com.rafaelduransaez.feature.ships.presentation.navigation.shipsGraph
import com.rafaelduransaez.swapi.ui.SwapiAppState

@Composable
fun SwapiNavHost(
    swapiAppState: SwapiAppState
) {
    val navController = swapiAppState.navController
    
    // Single NavHost that contains all navigation - this way the main app 
    // can see all navigation state for bottom bar and top bar management
    NavHost(
        navController = navController,
        startDestination = PlanetListNavigator,
    ) {
        // Planets feature navigation graph
        planetsGraph(
            onPlanetClick = navController::navigateToPlanetDetail,
            onBack = swapiAppState::navigateBack
        )
        
        // Ships feature navigation graph
        shipsGraph(
            onBack = swapiAppState::navigateBack
        )
    }
}