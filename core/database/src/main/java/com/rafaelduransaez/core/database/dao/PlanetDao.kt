package com.rafaelduransaez.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafaelduransaez.core.database.model.PlanetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planets: List<PlanetEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planet: PlanetEntity): Long

    @Query("SELECT * FROM planets")
    fun getAll(): Flow<List<PlanetEntity>>

    @Query("SELECT * FROM planets WHERE uid = :uid LIMIT 1")
    fun getPlanetById(uid: String): Flow<PlanetEntity?>

    @Query("SELECT MAX(lastUpdated) FROM planets")
    suspend fun getLastUpdated(): Long?
}