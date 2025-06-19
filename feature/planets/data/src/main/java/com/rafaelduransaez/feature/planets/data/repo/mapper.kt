package com.rafaelduransaez.feature.planets.data.repo

import com.rafaelduransaez.core.database.model.PlanetEntity
import com.rafaelduransaez.core.database.util.DatabaseError
import com.rafaelduransaez.core.network.utils.ApiError
import com.rafaelduransaez.feature.planets.data.remote.model.PlanetProperties
import com.rafaelduransaez.feature.planets.data.remote.model.PlanetResult
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel

fun List<PlanetEntity>.toPlanetSummaries(): List<PlanetSummaryModel> = map { it.toSummaryModel() }

fun PlanetEntity.toSummaryModel(): PlanetSummaryModel {
    return PlanetSummaryModel(
        uid = uid,
        name = name,
        climate = climate,
        population = population
    )
}

fun PlanetEntity.toDetailModel(): PlanetDetailModel {
    return PlanetDetailModel(
        uid = uid,
        name = name,
        climate = climate,
        population = population,
        diameter = diameter,
        gravity = gravity,
        terrain = terrain
    )
}

fun List<PlanetResult>.toPlanetEntities(currentTimestamp: Long): List<PlanetEntity> {
    return map { result ->
        result.properties.toPlanetEntity(result.uid, currentTimestamp)
    }
}

fun PlanetProperties.toPlanetEntity(uid: String, currentTimestamp: Long): PlanetEntity {
    return PlanetEntity(
        uid = uid,
        name = name,
        climate = climate,
        population = population.toIntOrNull() ?: 0,
        diameter = diameter.toDoubleOrNull() ?: 0.0,
        gravity = gravity,
        terrain = terrain,
        lastUpdated = currentTimestamp
    )
}

fun ApiError.toPlanetError(): PlanetError = when (this) {
    is ApiError.Unknown -> PlanetError.Unknown
    else -> PlanetError.ServerError
}

fun DatabaseError.toPlanetError(): PlanetError = when (this) {
    DatabaseError.NotFound -> PlanetError.DatabaseNotFoundError
    is DatabaseError.DatabaseNotAvailable,
    is DatabaseError.SqlSyntaxError -> PlanetError.DatabaseKindError
    is DatabaseError.Unknown -> PlanetError.Unknown
}
