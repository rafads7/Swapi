package com.rafaelduransaez.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapiToolbar(
    @StringRes titleId: Int,
    @StringRes contentDescription: Int = titleId,
    showIcon: Boolean = false,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onIconClick: () -> Unit = { }
) {
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = colorScheme.primaryContainer,
            titleContentColor = colorScheme.primary,
        ),
        title = { Text(text = stringResource(titleId)) },
        navigationIcon = {
            if (showIcon) {
                IconButton(onClick = onIconClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(contentDescription)
                    )
                }
            }
        }
    )
}