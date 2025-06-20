package com.rafaelduransaez.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenError(
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.ui_error_generic,
    showRetryButton: Boolean = true,
    @StringRes retryText: Int = R.string.ui_retry,
    onRetry: () -> Unit = {}
) {

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier.fillMaxSize().padding(12.dp)
            .focusRequester(focusRequester)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag("ErrorScreen"),
            text = stringResource(message),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (showRetryButton)
            Button(onClick = onRetry) { Text(stringResource(retryText)) }
    }
}