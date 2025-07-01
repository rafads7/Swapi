package com.rafaelduransaez.swapi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rafaelduransaez.swapi.navigation.SwapiNavHost
import com.rafaelduransaez.core.ui.DynamicTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapiApp(modifier: Modifier = Modifier, appState: SwapiAppState = rememberSwapiAppState()) {

    Scaffold(
        topBar = {
            DynamicTopAppBar(
                navController = appState.navController,
                scrollBehavior = appState.scrollBehavior,
            )
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                SwapiBottomBar(
                    currentDestination = appState.currentTopLevelDestination,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(colorScheme.onPrimary)
        ) {
            SwapiNavHost(appState)
        }
    }
}