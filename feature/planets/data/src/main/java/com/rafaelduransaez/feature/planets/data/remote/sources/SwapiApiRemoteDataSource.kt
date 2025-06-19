package com.rafaelduransaez.feature.planets.data.remote.sources

import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.mapSuccess
import com.rafaelduransaez.core.network.utils.ApiError
import com.rafaelduransaez.core.network.utils.safeApiCall
import com.rafaelduransaez.feature.planets.data.remote.api.SwapiPlanetsService
import com.rafaelduransaez.feature.planets.data.remote.model.PlanetResult
import javax.inject.Inject

class SwapiApiRemoteDataSource @Inject constructor(
    private val api: SwapiPlanetsService
): PlanetsRemoteDataSource {

    override suspend fun fetchPlanets(): SwapiResult<List<PlanetResult>, ApiError> {
        return safeApiCall { api.getPlanets() }.mapSuccess { it.results }
    }
}


