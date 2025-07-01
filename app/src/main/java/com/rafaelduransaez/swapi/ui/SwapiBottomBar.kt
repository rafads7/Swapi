package com.rafaelduransaez.swapi.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rafaelduransaez.swapi.navigation.TOP_LEVEL_DESTINATIONS
import com.rafaelduransaez.swapi.navigation.TopLevelDestination
import com.rafaelduransaez.swapi.ui.theme.SwapiTheme

@Composable
fun SwapiBottomBar(
    currentDestination: TopLevelDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            val selected = currentDestination == destination
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        },
                        contentDescription = stringResource(destination.contentDescriptionResId),
                    )
                },
                label = {
                    Text(stringResource(destination.titleResId))
                },
            )
        }
    }
}

@Preview
@Composable
private fun SwapiBottomBarPreview() {
    SwapiTheme {
        SwapiBottomBar(
            currentDestination = TopLevelDestination.Planets,
            onNavigateToDestination = {},
        )
    }
} 