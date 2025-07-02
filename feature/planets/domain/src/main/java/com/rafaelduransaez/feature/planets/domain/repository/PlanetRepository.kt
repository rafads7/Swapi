package com.rafaelduransaez.feature.planets.domain.repository

import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import kotlinx.coroutines.flow.Flow

interface PlanetRepository {
    suspend fun getPlanets(): Flow<SwapiResult<List<PlanetSummaryModel>, PlanetError>>
    suspend fun getPlanetById(uid: String): Flow<SwapiResult<PlanetDetailModel, PlanetError>>
    
    /**
     * Manually refresh planets data from remote source.
     * Useful for pull-to-refresh scenarios.
     * 
     * @return Result indicating success or failure of the refresh operation
     */
    suspend fun refreshPlanets(): SwapiResult<Unit, PlanetError>
}

