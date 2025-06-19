package com.rafaelduransaez.feature.planets.presentation

import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel

val mockPlanetSummaryList = listOf(
    PlanetSummaryModel(
        uid = "101",
        name = "Tatooine",
        climate = "Arid, Desert",
        population = 200000
    ),
    PlanetSummaryModel(
        uid = "102",
        name = "Alderaan",
        climate = "Temperate, Grasslands, Mountains",
        population = 2000000000
    ),
    PlanetSummaryModel(
        uid = "103",
        name = "Hoth",
        climate = "Frozen, Ice Plains, Tundra",
        population = 0 // Assuming no permanent settlements or very few
    ),
    PlanetSummaryModel(
        uid = "104",
        name = "Dagobah",
        climate = "Murky, Swamp, Humid",
        population = 0 // Or a very small, uncounted population
    ),
    PlanetSummaryModel(
        uid = "105",
        name = "Bespin",
        climate = "Temperate (Gas Giant Atmosphere)",
        population = 6000000
    ),
    PlanetSummaryModel(
        uid = "106",
        name = "Endor",
        climate = "Temperate, Forest, Mountains, Lakes",
        population = 30000000
    ),
    PlanetSummaryModel(
        uid = "107",
        name = "Naboo",
        climate = "Temperate, Swampy, Grassy Hills, Forests",
        population = 450000000
    ),
    PlanetSummaryModel(
        uid = "108",
        name = "Coruscant",
        climate = "Temperate (Artificial, City-Covered)",
        population = 1000000000 // A large number for a city planet
    ),
    PlanetSummaryModel(
        uid = "109",
        name = "Kamino",
        climate = "Temperate, Ocean World",
        population = 1000000000
    ),
    PlanetSummaryModel(
        uid = "110",
        name = "Geonosis",
        climate = "Arid, Rocky, Desert",
        population = 10000000
    ),
    PlanetSummaryModel(
        uid = "111",
        name = "Mustafar",
        climate = "Volcanic, Hot, Ash-covered",
        population = 20000 // Likely mining outposts
    ),
    PlanetSummaryModel(
        uid = "112",
        name = "Kashyyyk",
        climate = "Tropical, Forest, Humid",
        population = 45000000
    )
)
