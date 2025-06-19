package com.rafaelduransaez.feature.planets.domain.model

data class PlanetSummaryModel(
    val uid: String,
    val name: String,
    val climate: String,
    val population: Int
)
