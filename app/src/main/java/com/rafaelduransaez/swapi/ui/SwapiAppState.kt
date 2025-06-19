package com.rafaelduransaez.swapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rafaelduransaez.swapi.R
import com.rafaelduransaez.swapi.navigation.TopLevelDestination
import com.rafaelduransaez.ui.SwapiTopAppBarConfig
import com.rafaelduransaez.ui.SwapiTopAppBarController
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

@Stable
class SwapiAppState(
    val navController: NavHostController
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)
    private val defaultTopAppBarConfig = SwapiTopAppBarConfig(R.string.app_name)
    private val _topAppBarConfig = mutableStateOf(defaultTopAppBarConfig)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry =
                navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    var topAppBarConfig: SwapiTopAppBarConfig by _topAppBarConfig
        private set

    val topAppBarController: SwapiTopAppBarController = object : SwapiTopAppBarController {
        override fun setTopAppBar(config: SwapiTopAppBarConfig) {
            if (topAppBarConfig != config) {
                topAppBarConfig = config
            }
        }

        override fun resetTopAppBar() {
            if (topAppBarConfig != defaultTopAppBarConfig) {
                topAppBarConfig = defaultTopAppBarConfig
            }
        }
    }
    
    fun navigateBack() = navController.navigateUp()
}

@Composable
fun rememberSwapiAppState(
    navController: NavHostController = rememberNavController()
): SwapiAppState {
    return remember { SwapiAppState(navController) }
}
