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
import com.rafaelduransaez.core.database.util.safeDbFlowCall

import com.rafaelduransaez.feature.planets.data.local.PlanetsLocalDataSource
import com.rafaelduransaez.feature.planets.data.remote.sources.PlanetsRemoteDataSource
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetError.DatabaseKindError
import com.rafaelduransaez.feature.planets.domain.model.PlanetError.Unknown
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import com.rafaelduransaez.feature.planets.data.repo.RefreshState.Error as RefreshError
import com.rafaelduransaez.feature.planets.data.repo.RefreshState.Success as RefreshSuccess

/**
 * Refresh state for background operations
 */
sealed class RefreshState {
    data object Idle : RefreshState()
    data object Loading : RefreshState()
    data class Success(val timestamp: Long) : RefreshState()
    data class Error(val error: PlanetError, val timestamp: Long) : RefreshState()
}

/**
 * Improved Repository Implementation using Reactive Single Source of Truth Pattern with Refresh State Management
 *
 * Key Improvements:
 * 1. Database as single source of truth - UI always observes database
 * 2. Background refresh strategy with state communication
 * 3. Proper synchronization - prevents race conditions
 * 4. Optimized reactivity - reduces unnecessary emissions
 * 5. Enhanced error handling with UI notification
 * 6. Refresh state management for better UX
 */
@Singleton
class PlanetsRepositoryImpl @Inject constructor(
    private val localDataSource: PlanetsLocalDataSource,
    private val remoteDataSource: PlanetsRemoteDataSource,
    private val clock: Clock,
    @StaleTimeout private val staleTimeout: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PlanetRepository {

    // Mutex to prevent concurrent network calls
    private val refreshMutex = Mutex()

    // Refresh state management for UI communication
    private val _refreshState = MutableStateFlow<RefreshState>(RefreshState.Idle)
    val refreshState: StateFlow<RefreshState> = _refreshState.asStateFlow()

    /**
     * Reactive Single Source of Truth with Enhanced Error Communication
     *
     * Benefits:
     * - Single emission per data change (no double emission)
     * - Truly reactive - UI updates automatically when database changes
     * - Efficient - no unnecessary network calls
     * - Consistent state - database is always the source of truth
     * - Refresh state communication for better UX
     */
    override suspend fun getPlanets(): Flow<SwapiResult<List<PlanetSummaryModel>, PlanetError>> =
        fetchPlanetsFromLocal()
            .distinctUntilChanged()
            .onStart { refreshDataIfNeeded() }
            .flowOn(dispatcher)

    /**
     * Manual refresh method for pull-to-refresh or retry scenarios
     */
    override suspend fun refreshPlanets(): SwapiResult<Unit, PlanetError> = refreshMutex.withLock {
        _refreshState.value = RefreshState.Loading
        val timestamp = clock.currentTimeMillis()

        when (val result = fetchPlanetsFromRemote(timestamp)) {
            is Success -> {
                localDataSource.savePlanets(result.data)
                _refreshState.value = RefreshSuccess(timestamp)
                Success(Unit)
            }

            is Failure -> {
                _refreshState.value = RefreshError(result.error, timestamp)
                Failure(result.error)
            }
        }
    }

    private suspend fun refreshDataIfNeeded() {
        if (refreshMutex.isLocked) return

        refreshMutex.withLock {
            try {
                val now = clock.currentTimeMillis()
                val shouldRefresh = shouldRefreshData(now)

                if (shouldRefresh) {
                    _refreshState.value = RefreshState.Loading
                    refreshFromRemote(now)
                }
            } catch (e: Exception) {
                _refreshState.value = RefreshError(Unknown, clock.currentTimeMillis())
            }
        }
    }

    private suspend fun shouldRefreshData(currentTime: Long): Boolean {
        val lastUpdated = localDataSource.getLastUpdated()
        return when {
            lastUpdated == null || currentTime - lastUpdated > staleTimeout -> true
            else -> false
        }
    }

    private suspend fun refreshFromRemote(timestamp: Long) {
        fetchPlanetsFromRemote(timestamp).fold(
            onSuccess = { planets ->
                val savedItems = localDataSource.savePlanets(planets)
                if (savedItems.size < planets.size) {
                    _refreshState.value = RefreshError(DatabaseKindError, timestamp)
                } else {
                    _refreshState.value = RefreshSuccess(timestamp)
                }
            },
            onFailure = { error ->
                _refreshState.value = RefreshError(error, timestamp)
            }
        )
    }

    private fun fetchPlanetsFromLocal(): Flow<SwapiResult<List<PlanetSummaryModel>, PlanetError>> =
        safeDbFlowCall { localDataSource.getAllPlanets() }
            .mapSuccess { it.toPlanetSummaries() }
            .mapFailure { it.toPlanetError() }

    private suspend fun fetchPlanetsFromRemote(timestamp: Long): SwapiResult<List<PlanetEntity>, PlanetError> =
        remoteDataSource.fetchPlanets()
            .mapSuccess { it.toPlanetEntities(timestamp) }
            .mapFailure { it.toPlanetError() }

    override suspend fun getPlanetById(uid: String): Flow<SwapiResult<PlanetDetailModel, PlanetError>> {
        return safeDbFlowCall { localDataSource.getPlanetById(uid) }
            .mapSuccess { it.toDetailModel() }
            .mapFailure { it.toPlanetError() }
            .flowOn(dispatcher)
    }
}