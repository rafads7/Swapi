package com.rafaelduransaez.feature.planets.presentation.list

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.rafaelduransaez.core.ui.FullScreenError
import com.rafaelduransaez.core.ui.FullScreenLoadingIndicator
import com.rafaelduransaez.core.ui.ProvideAppBarTitle
import com.rafaelduransaez.core.ui.SwapiButton
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.feature.planets.presentation.list.Constants.FIRST_ITEM_INDEX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PlanetListScreen(
    uiState: PlanetListUiState,
    onUiEvent: (PlanetListUiEvent) -> Unit,
) {
    ProvideAppBarTitle {
        Text(text = stringResource(R.string.planets_title))
    }

    when (uiState) {
        PlanetListUiState.Loading -> FullScreenLoadingIndicator()
        is PlanetListUiState.Error ->
            FullScreenError(message = uiState.errorMessageId) {
                onUiEvent(PlanetListUiEvent.Retry)
            }

        is PlanetListUiState.ShowPlanets -> {
            PlanetListContent(
                modifier = Modifier.fillMaxSize(),
                planets = uiState.planets
            ) { uid -> onUiEvent(PlanetListUiEvent.ShowDetail(uid)) }
        }
    }
}

@Composable
fun PlanetListContent(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    planets: List<PlanetSummaryModel>,
    onPlanetClick: (planetUid: String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTopButton by remember { derivedStateOf { listState.firstVisibleItemIndex > FIRST_ITEM_INDEX } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        PlanetList(modifier.testTag("PlanetList"), listState, planets, onPlanetClick)
        ScrollToTopButton(
            showScrollToTopButton = showScrollToTopButton,
            coroutineScope = coroutineScope,
            listState = listState
        )
    }
}

@Composable
private fun PlanetList(
    modifier: Modifier,
    listState: LazyListState,
    planets: List<PlanetSummaryModel>,
    onPlanetClick: (planetUid: String) -> Unit
) {
    LazyColumn(
        modifier = modifier.semantics {
            contentDescription = "List of Star Wars planets"
        },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = listState
    ) {
        items(
            items = planets,
            key = { planet -> planet.uid }
        ) { planet ->
            PlanetListItem(
                planet = planet,
                onClick = { onPlanetClick(planet.uid) }
            )
        }
    }
}

@Composable
fun PlanetListItem(
    planet: PlanetSummaryModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {  }
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = planet.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.planet_population_val, planet.population),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.planet_climate_val, planet.climate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.planet_go_to_details),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun BoxScope.ScrollToTopButton(
    modifier: Modifier = Modifier,
    showScrollToTopButton: Boolean,
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    @StringRes textId: Int = R.string.planets_scroll_to_top_button_text

) {
    AnimatedVisibility(showScrollToTopButton, modifier = Modifier.align(Alignment.TopCenter)) {
        SwapiButton(
            modifier = modifier.testTag("ScrollToTopButton").align(Alignment.TopCenter),
            textId = textId
        ) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }
}

object Constants {
    const val FIRST_ITEM_INDEX = 0
}