package com.rafaelduransaez.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rafaelduransaez.core.database.dao.PlanetDao
import com.rafaelduransaez.core.database.model.PlanetEntity

@Database(entities = [PlanetEntity::class], version = 1)
abstract class SwapiDataBase : RoomDatabase() {
    abstract fun planetDao(): PlanetDao
}