package com.rafaelduransaez.core.database.di

import com.rafaelduransaez.core.database.SwapiDataBase
import com.rafaelduransaez.core.database.dao.PlanetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun providesPlanetPointDao(database: SwapiDataBase): PlanetDao = database.planetDao()
}
