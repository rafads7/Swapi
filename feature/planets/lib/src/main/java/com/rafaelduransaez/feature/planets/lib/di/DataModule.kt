package com.rafaelduransaez.feature.planets.lib.di

import com.rafaelduransaez.core.database.dao.PlanetDao
import com.rafaelduransaez.core.network.di.SwapiRetrofitClient
import com.rafaelduransaez.feature.planets.data.remote.api.SwapiPlanetsService
import com.rafaelduransaez.feature.planets.data.local.PlanetsLocalDataSource
import com.rafaelduransaez.feature.planets.data.remote.sources.PlanetsRemoteDataSource
import com.rafaelduransaez.feature.planets.data.local.PlanetsRoomDataSource
import com.rafaelduransaez.feature.planets.data.remote.sources.SwapiApiRemoteDataSource
import com.rafaelduransaez.feature.planets.data.repo.PlanetsRepositoryImpl
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSwapiPlanetsService(@SwapiRetrofitClient retrofit: Retrofit): SwapiPlanetsService {
        return retrofit.create(SwapiPlanetsService::class.java)
    }

    @Provides
    @Singleton
    fun providePlanetsRemoteDataSource(api: SwapiPlanetsService): PlanetsRemoteDataSource =
        SwapiApiRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePlanetsLocalDataSource(dao: PlanetDao): PlanetsLocalDataSource =
        PlanetsRoomDataSource(dao)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlanetRepository(planetsRepositoryImpl: PlanetsRepositoryImpl): PlanetRepository
}
