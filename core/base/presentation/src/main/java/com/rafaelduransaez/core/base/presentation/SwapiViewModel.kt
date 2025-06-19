package com.rafaelduransaez.core.base.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class SwapiViewModel<NavEffectState> : ViewModel() {

    private val _navState = Channel<NavEffectState>()
    val navState = _navState.receiveAsFlow()

    open fun navigateTo(navDestination: NavEffectState) {
        viewModelScope.launch {
            _navState.send(navDestination)
        }
    }
}