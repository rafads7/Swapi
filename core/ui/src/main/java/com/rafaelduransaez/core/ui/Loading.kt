package com.rafaelduransaez.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
    showLoadingMessage: Boolean = false,
    @StringRes messageId: Int = R.string.ui_loading
) {
    Column(
        modifier = modifier.fillMaxSize().semantics(true) {
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            liveRegion = LiveRegionMode.Polite
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier.testTag("LoadingIndicator"))
        Spacer(modifier = Modifier.height(8.dp))

        if (showLoadingMessage)
            Text(stringResource(messageId))
    }
}