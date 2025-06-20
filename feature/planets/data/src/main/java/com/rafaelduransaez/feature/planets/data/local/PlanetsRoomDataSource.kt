package com.rafaelduransaez.feature.planets.data.local

import com.rafaelduransaez.core.database.dao.PlanetDao
import com.rafaelduransaez.core.database.model.PlanetEntity
import kotlinx.coroutines.flow.Flow

class PlanetsRoomDataSource(
    private val planetsDao: PlanetDao
) : PlanetsLocalDataSource {

    override suspend fun getLastUpdated(): Long? = planetsDao.getLastUpdated()

    override fun getAllPlanets(): Flow<List<PlanetEntity>> = planetsDao.getAll()

    override fun getPlanetById(uid: String): Flow<PlanetEntity?> = planetsDao.getPlanetById(uid)

    override suspend fun savePlanets(planets: List<PlanetEntity>): List<Long> = planetsDao.insert(planets)

    override suspend fun savePlanet(planet: PlanetEntity): Boolean = planetsDao.insert(planet) > 0

}