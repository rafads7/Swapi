package com.rafaelduransaez.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun NonEditableTextField(
    modifier: Modifier = Modifier,
    text: String,
    @StringRes labelId: Int
) {
    SwapiLabelValueText(modifier, text, labelId)
}

@Composable
fun NonEditableTextField(
    modifier: Modifier = Modifier,
    text: Double,
    @StringRes labelId: Int
) {
    SwapiLabelValueText(modifier, text.toString(), labelId)
}

@Composable
fun NonEditableTextField(
    modifier: Modifier = Modifier,
    text: Int,
    @StringRes labelId: Int
) {
    SwapiLabelValueText(modifier, text.toString(), labelId)
}

@Composable
fun SwapiLabelValueText(
    modifier: Modifier = Modifier,
    text: String,
    @StringRes labelId: Int
) {

    Column(
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 2.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}