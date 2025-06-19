package com.rafaelduransaez.feature.planets.presentation.list.mapper

import com.rafaelduransaez.feature.planets.domain.model.PlanetError

interface PlanetListUiMapper {
    fun getErrorMessageId(error: PlanetError): Int
}

