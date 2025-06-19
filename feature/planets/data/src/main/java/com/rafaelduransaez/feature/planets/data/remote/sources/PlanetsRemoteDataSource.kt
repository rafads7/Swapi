package com.rafaelduransaez.feature.planets.data.remote.sources

import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.network.utils.ApiError
import com.rafaelduransaez.feature.planets.data.remote.model.PlanetResult

interface PlanetsRemoteDataSource {

    suspend fun fetchPlanets(): SwapiResult<List<PlanetResult>, ApiError>
}