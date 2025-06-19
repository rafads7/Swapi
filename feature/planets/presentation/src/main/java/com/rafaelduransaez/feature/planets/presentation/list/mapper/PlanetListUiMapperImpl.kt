package com.rafaelduransaez.feature.planets.presentation.list.mapper

import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.presentation.R
import javax.inject.Inject

class PlanetListUiMapperImpl @Inject constructor(): PlanetListUiMapper {

    override fun getErrorMessageId(error: PlanetError): Int {
        return when(error) {
            PlanetError.DatabaseNotFoundError -> R.string.planets_error_database_not_found
            PlanetError.DatabaseKindError -> R.string.planets_error_database_unknown
            PlanetError.ServerError -> R.string.planets_error_server
            PlanetError.Unknown -> R.string.planets_error_unknown
        }
    }
}