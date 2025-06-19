package com.rafaelduransaez.feature.planets.presentation.detail.mapper

import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetFeature

interface PlanetDetailUiMapper {
    fun map(model: PlanetDetailModel): List<PlanetFeature>
    fun getErrorMessageId(error: PlanetError): Int
}

