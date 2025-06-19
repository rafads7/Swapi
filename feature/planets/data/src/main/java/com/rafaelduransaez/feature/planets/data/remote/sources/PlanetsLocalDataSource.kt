package com.rafaelduransaez.feature.planets.data.remote.sources

import com.rafaelduransaez.core.database.model.PlanetEntity
import kotlinx.coroutines.flow.Flow

interface PlanetsLocalDataSource {
    suspend fun getLastUpdated(): Long?
    fun getAllPlanets(): Flow<List<PlanetEntity>>
    fun getPlanetById(uid: String): Flow<PlanetEntity?>
    suspend fun savePlanets(planets: List<PlanetEntity>):  List<Long>
    suspend fun savePlanet(planet: PlanetEntity): Boolean
}