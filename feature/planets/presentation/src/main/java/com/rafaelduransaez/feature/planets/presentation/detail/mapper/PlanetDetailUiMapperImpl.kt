package com.rafaelduransaez.feature.planets.presentation.detail.mapper

import com.rafaelduransaez.core.common.formatter.NumberFormatter
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.presentation.R
import com.rafaelduransaez.feature.planets.presentation.detail.PlanetFeature
import javax.inject.Inject

class PlanetDetailUiMapperImpl @Inject constructor(
    private val numberFormatter: NumberFormatter
): PlanetDetailUiMapper {

    override fun map(model: PlanetDetailModel) = listOf(
        PlanetFeature(
            label = R.string.planets_climate,
            value = model.climate.replaceFirstChar { it.uppercase() }
        ),
        PlanetFeature(
            label = R.string.planets_terrain,
            value = model.terrain.replaceFirstChar { it.uppercase() }
        ),
        PlanetFeature(
            label = R.string.planets_population,
            value = numberFormatter.format(model.population)
        ),
        PlanetFeature(
            label = R.string.planets_diameter,
            value = numberFormatter.format(model.diameter)
        ),
        PlanetFeature(
            label = R.string.planets_gravity,
            value = model.gravity.replaceFirstChar { it.uppercase() }
        )
    )

    override fun getErrorMessageId(error: PlanetError): Int {
        return when(error) {
            PlanetError.DatabaseNotFoundError -> R.string.planets_error_database_not_found
            PlanetError.DatabaseKindError -> R.string.planets_error_database_unknown
            PlanetError.ServerError -> R.string.planets_error_server
            PlanetError.Unknown -> R.string.planets_error_unknown
        }
    }
}