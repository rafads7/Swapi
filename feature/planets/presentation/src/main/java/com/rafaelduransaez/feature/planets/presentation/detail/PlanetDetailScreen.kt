package com.rafaelduransaez.feature.planets.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.ui.FullScreenError
import com.rafaelduransaez.ui.FullScreenLoadingIndicator
import com.rafaelduransaez.ui.SwapiToolbar

@Composable
fun PlanetDetailScreen(
    uiState: PlanetDetailUiState,
    onUiEvent: (PlanetDetailUiEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            PlanetDetailUiState.Loading -> FullScreenLoadingIndicator()
            is PlanetDetailUiState.Error -> FullScreenError(message = uiState.errorMessageId) {
                onUiEvent(PlanetDetailUiEvent.Retry)
            }

            is PlanetDetailUiState.Success -> {
                PlanetDetailsContent(features = uiState.planetFeatures)
            }
        }
    }
}

@Composable
fun PlanetDetailsContent(
    modifier: Modifier = Modifier,
    features: List<PlanetFeature>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(features) { feature ->
            PlanetFeatureItem(feature = feature)
        }
    }
}


@Composable
fun PlanetFeatureItem(
    feature: PlanetFeature,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(feature.label),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = feature.value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}