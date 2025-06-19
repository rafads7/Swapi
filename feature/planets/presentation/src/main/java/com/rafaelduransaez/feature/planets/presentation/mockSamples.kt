package com.rafaelduransaez.feature.planets.presentation

import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel

val mockSamples = listOf(
    PlanetSummaryModel(uid = "5", "Bespin", "Cloudy", 600000)
)

val mockPlanetDetails: List<PlanetDetailModel> = listOf(
    PlanetDetailModel(
        uid = "1",
        name = "Tatooine",
        climate = "Arid",
        population = 200000,
        diameter = 10465.0,
        gravity = "gravity example",
        terrain = "Desert, Rock, Dunes"
    ),
    PlanetDetailModel(
        uid = "2",
        name = "Alderaan",
        climate = "Temperate",
        population = 2000000000, // Using a larger, more typical population for Alderaan
        diameter = 12500.0,
        gravity = "gravity example",
        terrain = "Grasslands, Mountains, Forests, Oceans"
    ),
    PlanetDetailModel(
        uid = "3",
        name = "Hoth",
        climate = "Frozen",
        population = 0, // Assuming no significant permanent population
        diameter = 7200.0,
        gravity = "gravity example",
        terrain = "Ice plains, Glaciers, Snowy mountains, Caves"
    ),
    PlanetDetailModel(
        uid = "4",
        name = "Dagobah",
        climate = "Swampy", // Corrected from your initial PlanetModel list for Dagobah
        population = 0,     // Assuming no significant population
        diameter = 8900.0,
        gravity = "gravity example",
        terrain = "Swamps, Murky bogs, Dense jungles, Caves"
    ),
    PlanetDetailModel(
        uid = "5",
        name = "Bespin",
        climate = "Gas Giant (Cloud City in temperate layer)", // More descriptive
        population = 6000000, // Population for Cloud City and other floating structures
        diameter = 118000.0, // Diameter of the gas giant itself
        gravity = "gravity example",     // At a certain atmospheric level where Cloud City floats
        terrain = "Gas, Tibanna Gas Clouds (Floating cities)"
    )
)
