package com.rafaelduransaez.swapi.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rafaelduransaez.swapi.navigation.BOTTOM_BAR_ROUTES
import com.rafaelduransaez.swapi.navigation.TopLevelDestination
import com.rafaelduransaez.swapi.navigation.TOP_LEVEL_DESTINATIONS

@Stable
class SwapiAppState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val navController: NavHostController,
    val scrollBehavior: TopAppBarScrollBehavior
) {
    // Clean route-based bottom bar logic using BOTTOM_BAR_ROUTES
    private val previousDestination = mutableStateOf<NavDestination?>(null)

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

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            val currentRoute = currentDestination?.route
            return TOP_LEVEL_DESTINATIONS.find { destination ->
                // Check if we're currently at this destination's baseRoute (the navigator)
                // or any of its child routes (like the main list screen)
                currentRoute == destination.baseRoute.qualifiedName ||
                currentRoute == destination.route.qualifiedName
                
                // TODO: Use hierarchy-based approach when API is available:
                // currentDestination?.hierarchy?.any { it.hasRoute(destination.baseRoute::class) } == true ||
                // currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
            }
        }

    /**
     * Determines if the bottom bar should be shown.
     * Bottom bar is shown only for routes defined in BOTTOM_BAR_ROUTES.
     * This automatically hides it for detail screens and nested destinations.
     * To add a new feature that should show the bottom bar, add its main list route to BOTTOM_BAR_ROUTES.
     */
    val shouldShowBottomBar: Boolean
        @Composable get() {
            val currentRoute = currentDestination?.route
            return currentRoute in BOTTOM_BAR_ROUTES
        }

    fun navigateBack() = navController.navigateUp()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navController.navigate(topLevelDestination.getDestinationObject()) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSwapiAppState(
    navController: NavHostController = rememberNavController(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
): SwapiAppState {
    return remember { SwapiAppState(navController, scrollBehavior) }
}
