package com.rafaelduransaez.ui

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun SwapiButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    enabled: Boolean = true,
    containerColor: Color = colorScheme.tertiaryContainer,
    textColor: Color = colorScheme.tertiary,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        enabled = enabled,
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(textId),
            color = colorScheme.tertiary
        )
    }
}