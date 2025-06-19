package com.rafaelduransaez.swapi.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetList
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetListNavigator
import com.rafaelduransaez.swapi.R
import kotlin.reflect.KClass

enum class TopLevelDestination (
    val selectedIcon: ImageVector, //in case needed for navigation UI in future
    val unselectedIcon: ImageVector, //in case needed for navigation UI in future
    @StringRes val iconTextId: Int, //in case needed for navigation UI in future
    @StringRes val titleTextId: Int, //in case needed for navigation UI in future
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    PLANETS(
        selectedIcon = Icons.Default.Place,
        unselectedIcon = Icons.Outlined.Place,
        iconTextId = R.string.swapi_planets,
        titleTextId = R.string.swapi_planets,
        route = PlanetList::class,
        baseRoute = PlanetListNavigator::class,
    )
}