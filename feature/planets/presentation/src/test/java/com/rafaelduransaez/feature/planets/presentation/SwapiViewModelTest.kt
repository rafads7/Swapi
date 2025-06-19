package com.rafaelduransaez.feature.planets.presentation

import com.rafaelduransaez.core.base.presentation.SwapiViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule

open class SwapiViewModelTest<VM: SwapiViewModel<NavState>, UiState, NavState> {

    @ExperimentalCoroutinesApi
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    protected lateinit var viewModel: VM
    protected lateinit var emittedStates: MutableList<UiState>
    protected lateinit var navStates: MutableList<NavState>

    @Before
    open fun setUp() {
        emittedStates = mutableListOf()
        navStates = mutableListOf()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun TestScope.collectViewModelState(uiState: Flow<UiState>): Job {
        return backgroundScope.launch(UnconfinedTestDispatcher()) {
            uiState.collect { uiState ->
                emittedStates.add(uiState)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun TestScope.collectNavState(viewModel: VM): Job =
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.navState.collect { navState ->
                navStates.add(navState)
            }
        }
}