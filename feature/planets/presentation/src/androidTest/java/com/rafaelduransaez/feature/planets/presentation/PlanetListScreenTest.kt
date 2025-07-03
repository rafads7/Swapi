package com.rafaelduransaez.feature.planets.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListContent
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListScreen
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiEvent
import com.rafaelduransaez.feature.planets.presentation.list.PlanetListUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PlanetListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun whenStateIsLoading_loadingIndicatorIsShown() {
        // Arrange
        composeTestRule.setContent {
            PlanetListScreen(
                uiState = PlanetListUiState(isLoading = true),
                onUiEvent = {}
            )
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_errorComponentIsShownAndRetryIsClickable() {
        var wasRetryClicked = false
        val errorMessageId = R.string.planets_error_server

        composeTestRule.setContent {
            PlanetListScreen(
                uiState = PlanetListUiState(
                    isLoading = false,
                    errorMessageId = errorMessageId
                ),
                onUiEvent = { event ->
                    if (event is PlanetListUiEvent.Retry) {
                        wasRetryClicked = true
                    }
                }
            )
        }

        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue("Retry callback was not triggered", wasRetryClicked)
    }
    
    @Test
    fun whenStateIsShowPlanets_planetListIsDisplayed() {
        val planets = createMockPlanetList(5)

        composeTestRule.setContent {
            PlanetListScreen(
                uiState = PlanetListUiState(
                    isLoading = false,
                    errorMessageId = null,
                    allPlanets = planets,
                    filteredPlanets = planets,
                    searchQuery = ""
                ),
                onUiEvent = {}
            )
        }

        composeTestRule.onNodeWithText("Planet 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Planet 5").assertIsDisplayed()
    }

    @Test
    fun planetListItem_whenClicked_invokesCallbackWithCorrectUid() {
        
        var clickedPlanetUid: String? = null
        val planets = createMockPlanetList(1) // Just need one planet for this test

        composeTestRule.setContent {
            PlanetListContent (
                planets = planets,
                searchQuery = "",
                onSearchQueryChange = {},
                onPlanetClick = { uid ->
                    clickedPlanetUid = uid
                }
            )
        }

        composeTestRule.onNodeWithText("Planet 1").performClick()

        assertEquals("1", clickedPlanetUid)
    }

    @Test
    fun scrollToTopButton_isNotVisibleInitially_andAppearsAfterScrolling() {
        val planets = createMockPlanetList(20) // Need enough items to scroll

        composeTestRule.setContent {
            PlanetListContent(
                planets = planets,
                searchQuery = "",
                onSearchQueryChange = {},
                onPlanetClick = {}
            )
        }

        composeTestRule.onNodeWithTag("ScrollToTopButton").assertIsNotDisplayed()

        composeTestRule.onNodeWithTag("PlanetList").performScrollToIndex(15)

        composeTestRule.onNodeWithTag("ScrollToTopButton").assertIsDisplayed()
    }

    @Test
    fun scrollToTopButton_whenClicked_scrollsListToTop() {
        val planets = createMockPlanetList(20)

        composeTestRule.setContent {
            PlanetListContent(
                planets = planets,
                searchQuery = "",
                onSearchQueryChange = {},
                onPlanetClick = {}
            )
        }

        composeTestRule.onNodeWithTag("PlanetList").performScrollToIndex(15)
        composeTestRule.onNodeWithTag("ScrollToTopButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()

        composeTestRule.onNodeWithText("Planet 1").assertIsDisplayed()
    }
}

private fun createMockPlanetList(count: Int): List<PlanetSummaryModel> {
    return (1..count).map {
        PlanetSummaryModel(
            uid = it.toString(),
            name = "Planet $it",
            population = (it * 1000),
            climate = "temperate"
        )
    }
}