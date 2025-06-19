package com.rafaelduransaez.feature.planets.domain.model

import com.rafaelduransaez.core.base.common.SwapiFailure

sealed interface PlanetError : SwapiFailure {
    data object ServerError : PlanetError
    data object DatabaseNotFoundError : PlanetError
    data object DatabaseKindError : PlanetError
    data object Unknown : PlanetError
}
