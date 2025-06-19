package com.rafaelduransaez.core.database.di

import android.content.Context
import androidx.room.Room
import com.rafaelduransaez.core.database.SwapiDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): SwapiDataBase =
        Room.databaseBuilder(
            context, SwapiDataBase::class.java, "swapi-database"
        ).build()
}