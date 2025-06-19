package com.rafaelduransaez.feature.planets.domain.model

data class PlanetDetailModel(
    val uid: String,
    val name: String,
    val climate: String,
    val population: Int,
    val diameter: Double,
    val gravity: String,
    val terrain: String
)