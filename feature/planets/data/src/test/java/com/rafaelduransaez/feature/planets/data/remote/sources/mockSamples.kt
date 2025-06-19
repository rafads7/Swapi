package com.rafaelduransaez.feature.planets.data.remote.sources

import com.rafaelduransaez.core.database.model.PlanetEntity

val tatooine = PlanetEntity(
    uid = "1",
    name = "Tatooine",
    climate = "arid",
    population = 200000,
    diameter = 10465.0,
    gravity = "1 standard",
    terrain = "desert",
    lastUpdated = System.currentTimeMillis()
)

val alderaan = PlanetEntity(
    uid = "2",
    name = "Alderaan",
    climate = "temperate",
    population = 2000000000,
    diameter = 12500.0,
    gravity = "1 standard",
    terrain = "grasslands, mountains",
    lastUpdated = System.currentTimeMillis()
)

val hoth = PlanetEntity(
    uid = "3",
    name = "Hoth",
    climate = "frozen",
    population = 0,
    diameter = 7200.0,
    gravity = "1.1 standard",
    terrain = "tundra, ice caves, mountain ranges",
    lastUpdated = System.currentTimeMillis()
)
