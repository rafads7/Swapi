package com.rafaelduransaez.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

val LocalTopAppBarController = staticCompositionLocalOf<SwapiTopAppBarController> {
    error("No TopAppBarController provided")
}

interface SwapiTopAppBarController {
    fun setTopAppBar(config: SwapiTopAppBarConfig)
    fun resetTopAppBar()
}

data class SwapiTopAppBarConfig(
    @StringRes val titleResId: Int,
    val navIcon: ImageVector? = null,
    val onNavIconClicked: () -> Unit = { },
    val actions: @Composable RowScope.() -> Unit = {}
)