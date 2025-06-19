package com.rafaelduransaez.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val climate: String,
    val population: Int,
    val diameter: Double,
    val gravity: String,
    val terrain: String,
    val lastUpdated: Long
)