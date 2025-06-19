package com.rafaelduransaez.swapi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rafaelduransaez.swapi.R
import com.rafaelduransaez.swapi.navigation.SwapiNavHost
import com.rafaelduransaez.ui.LocalTopAppBarController
import com.rafaelduransaez.ui.SwapiTopAppBarConfig

@Composable
fun SwapiApp(modifier: Modifier = Modifier, appState: SwapiAppState = rememberSwapiAppState()) {

    CompositionLocalProvider(LocalTopAppBarController provides appState.topAppBarController) {
        Scaffold(
            topBar = { SwapiTopAppBar(appState.topAppBarConfig) }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapiTopAppBar(topAppBarConfig: SwapiTopAppBarConfig) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = colorScheme.primaryContainer,
            titleContentColor = colorScheme.primary,
        ),
        title = { Text(text = stringResource(topAppBarConfig.titleResId)) },
        navigationIcon = {
            topAppBarConfig.navIcon?.let {
                IconButton(onClick = topAppBarConfig.onNavIconClicked) {
                    Icon(
                        imageVector = it,
                        contentDescription = stringResource(topAppBarConfig.titleResId)
                    )
                }
            }
        }
    )
}