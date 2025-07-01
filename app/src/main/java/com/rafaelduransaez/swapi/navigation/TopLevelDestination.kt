package com.rafaelduransaez.swapi.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Build
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetList
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetListNavigator
import com.rafaelduransaez.feature.ships.presentation.navigation.ShipList
import com.rafaelduransaez.feature.ships.presentation.navigation.ShipListNavigator
import com.rafaelduransaez.swapi.R
import kotlinx.serialization.Serializable

/**
 * Top level destinations configuration for bottom navigation.
 * 
 * This sealed class provides a scalable approach to managing bottom navigation:
 * - baseRoute: The navigator KClass used for navigation between features
 * - route: The main list screen KClass that should show the bottom bar
 * - BOTTOM_BAR_ROUTES is automatically derived from route properties
 * 
 * To add a new feature:
 * 1. Add a new data object to this sealed class
 * 2. Specify its navigator and list screen routes
 * 3. Bottom bar visibility and navigation will work automatically
 */
sealed class TopLevelDestination(
    val baseRoute: KClass<*>, // Navigator route for navigation
    val route: KClass<*>, // Base list screen route for bottom bar visibility
    @StringRes val titleResId: Int,
    @StringRes val contentDescriptionResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object Planets : TopLevelDestination(
        baseRoute = PlanetListNavigator::class,
        route = PlanetList::class,
        titleResId = R.string.bottom_nav_planets,
        contentDescriptionResId = R.string.bottom_nav_planets_description,
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
    )
    
    data object Ships : TopLevelDestination(
        baseRoute = ShipListNavigator::class,
        route = ShipList::class,
        titleResId = R.string.bottom_nav_ships,
        contentDescriptionResId = R.string.bottom_nav_ships_description,
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build,
    )
    
    /**
     * Helper to get the actual destination object for navigation.
     * Maps from KClass to the actual destination object.
     */
    fun getDestinationObject(): Any = when (this) {
        Planets -> PlanetListNavigator
        Ships -> ShipListNavigator
    }
}

// ShipsPlaceholder is no longer needed as we now have the real ships feature

/**
 * List of all top level destinations in the app
 */
val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination.Planets,
    TopLevelDestination.Ships,
)

/**
 * Routes that should show the bottom bar.
 * Automatically derived from TOP_LEVEL_DESTINATIONS route property.
 * To add a new feature that shows bottom bar, simply add it to TOP_LEVEL_DESTINATIONS.
 */
val BOTTOM_BAR_ROUTES = TOP_LEVEL_DESTINATIONS.map { it.route.qualifiedName }.toSet()