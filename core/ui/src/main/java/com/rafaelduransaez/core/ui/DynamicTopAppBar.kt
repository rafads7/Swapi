package com.rafaelduransaez.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopAppBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    backStackEntry?.let { entry ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { TopAppBarViewModel() },
        )

        TopAppBar(
            modifier = modifier,
            title = viewModel.title,
            navigationIcon = viewModel.navigationIcon,
            actions = viewModel.actions,
            scrollBehavior = scrollBehavior,
            colors = topAppBarColors(
                containerColor = colorScheme.primaryContainer,
                titleContentColor = colorScheme.primary,
            )
        )
    }
}

/**
 * A private, generic helper to reduce boilerplate in the 'Provide' functions.
 *
 * It retrieves the NavBackStackEntry-scoped ViewModel and uses a LaunchedEffect
 * to update a specific property on it.
 *
 * @param T The type of the value being provided (e.g., a composable lambda).
 * @param value The actual content to set, used as the key for LaunchedEffect.
 * @param updateAction A lambda that specifies which ViewModel property to update.
 */
@Composable
private fun <T> ProvideAppBarProperty(
    value: T,
    updateAction: (viewModel: TopAppBarViewModel, value: T) -> Unit
) {
    // Get the NavBackStackEntry if the current screen is from NavHost
    val owner = LocalViewModelStoreOwner.current as? NavBackStackEntry
    owner?.let {
        // Retrieve the ViewModel scoped to this specific screen
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = it,
            initializer = { TopAppBarViewModel() },
        )

        // Update the ViewModel property whenever the provided value instance changes
        LaunchedEffect(value) {
            updateAction(viewModel, value)
        }
    }
}

/**
 * Configures the screen-specific TopAppBar from within any feature composable.
 *
 * All parameters are optional. Only non-null parameters will be updated.
 * This allows a screen to set just the title, just the actions, or any combination.
 *
 * @param title An optional composable lambda for the app bar's title.
 * @param navigationIcon An optional composable lambda for the navigation icon.
 * @param actions An optional composable lambda for the action icons.
 */
@Composable
fun ProvideTopAppBar(
    title: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    // Get the NavBackStackEntry if the current screen is from NavHost
    val owner = LocalViewModelStoreOwner.current as? NavBackStackEntry
    owner?.let {
        // Retrieve the ViewModel scoped to this specific screen
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = it,
            initializer = { TopAppBarViewModel() },
        )

        // Each property is updated in its own LaunchedEffect.
        // This ensures that we only update what's provided and that the effect
        // re-runs only when its specific content lambda changes.

        if (title != null) {
            LaunchedEffect(title) {
                viewModel.title = title
            }
        }

        if (navigationIcon != null) {
            LaunchedEffect(navigationIcon) {
                viewModel.navigationIcon = navigationIcon
            }
        }

        if (actions != null) {
            LaunchedEffect(actions) {
                viewModel.actions = actions
            }
        }
    }
}

@Composable
fun ProvideAppBarTitle(title: @Composable () -> Unit) {
    ProvideAppBarProperty(value = title) { viewModel, newTitle ->
        viewModel.title = newTitle
    }
}

@Composable
fun ProvideAppBarNavigationIcon(navigationIcon: @Composable () -> Unit) {
    ProvideAppBarProperty(value = navigationIcon) { viewModel, newIcon ->
        viewModel.navigationIcon = newIcon
    }
}

@Composable
fun ProvideAppBarActions(actions: @Composable RowScope.() -> Unit) {
    ProvideAppBarProperty(value = actions) { viewModel, newActions ->
        viewModel.actions = newActions
    }
}

private class TopAppBarViewModel : ViewModel() {
    var title by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
    var navigationIcon by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
    var actions by mutableStateOf<@Composable RowScope.() -> Unit>({ }, referentialEqualityPolicy())
}
