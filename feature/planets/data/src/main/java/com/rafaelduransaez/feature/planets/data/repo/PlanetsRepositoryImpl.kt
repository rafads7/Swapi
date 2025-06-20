package com.rafaelduransaez.feature.planets.data.repo

import android.database.SQLException
import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.SwapiResult.Failure
import com.rafaelduransaez.core.base.common.SwapiResult.Success
import com.rafaelduransaez.core.base.common.mapFailure
import com.rafaelduransaez.core.base.common.mapSuccess
import com.rafaelduransaez.core.common.di.IoDispatcher
import com.rafaelduransaez.core.common.di.StaleTimeout
import com.rafaelduransaez.core.common.time.Clock
import com.rafaelduransaez.core.database.model.PlanetEntity
import com.rafaelduransaez.core.database.util.DatabaseError
import com.rafaelduransaez.core.database.util.safeDbCall
import com.rafaelduransaez.core.database.util.safeDbFlowCall
import com.rafaelduransaez.feature.planets.data.local.PlanetsLocalDataSource
import com.rafaelduransaez.feature.planets.data.remote.sources.PlanetsRemoteDataSource
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanetsRepositoryImpl @Inject constructor(
    private val localDataSource: PlanetsLocalDataSource,
    private val remoteDataSource: PlanetsRemoteDataSource,
    private val clock: Clock,
    @StaleTimeout private val staleTimeout: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PlanetRepository {

    override suspend fun getPlanets(): Flow<SwapiResult<List<PlanetSummaryModel>, PlanetError>> =
        flow {
            val cachedPlanets = fetchDataFromLocal()

            // Always emit cached planets if available
            if (!cachedPlanets.isNullOrEmpty()) {
                emit(Success(cachedPlanets.toPlanetSummaries()))
            }

            val lastUpdated = localDataSource.getLastUpdated()
            val now = clock.currentTimeMillis()
            val isStale = lastUpdated == null || now - lastUpdated > staleTimeout

            if (cachedPlanets.isNullOrEmpty() || isStale) {
                when (val result = fetchDataFromRemote(now)) {
                    is Success -> {
                        val savedPlanets = updateLocallyFromRemote(result)
                        emit(Success(savedPlanets.toPlanetSummaries()))
                    }

                    is Failure -> {
                        // If fetching fails there was no cache, emit failure
                        if (cachedPlanets.isNullOrEmpty()) {
                            emit(result)
                        }
                    }
                }
            }
        }
            .flowOn(dispatcher)
            .catch { e ->
                when (e) {
                    is CancellationException -> throw e
                    is SQLException -> emit(Failure(PlanetError.DatabaseKindError))
                    else -> emit(Failure(PlanetError.Unknown))
                }
            }

    private suspend fun fetchDataFromLocal(): List<PlanetEntity>? =
        localDataSource.getAllPlanets().firstOrNull()

    private suspend fun updateLocallyFromRemote(result: Success<List<PlanetEntity>>): List<PlanetEntity> {
        val entitiesToSave = result.data
        localDataSource.savePlanets(entitiesToSave)
        return entitiesToSave
    }

    private suspend fun fetchDataFromRemote(now: Long): SwapiResult<List<PlanetEntity>, PlanetError> =
        remoteDataSource.fetchPlanets()
            .mapSuccess { it.toPlanetEntities(now) }
            .mapFailure { it.toPlanetError() }


    override suspend fun getPlanetById(uid: String): Flow<SwapiResult<PlanetDetailModel, PlanetError>> {
        return safeDbFlowCall { localDataSource.getPlanetById(uid) }
            .mapSuccess { it.toDetailModel() }
            .mapFailure { it.toPlanetError() }
            .flowOn(dispatcher)
    }

}