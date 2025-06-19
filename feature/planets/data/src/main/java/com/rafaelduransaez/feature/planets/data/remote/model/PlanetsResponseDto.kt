package com.rafaelduransaez.feature.planets.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanetsResponseDto(
    val message: String,
    @SerialName("total_records")
    val totalRecords: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    val previous: String?,
    val next: String?,
    val results: List<PlanetResult>,
    @SerialName("apiVersion")
    val apiVersion: String,
    val timestamp: String,
    val support: Support,
    val social: Social
)

@Serializable
data class PlanetResult(
    val properties: PlanetProperties,
    @SerialName("_id")
    val id: String,
    val description: String,
    val uid: String,
    @SerialName("__v")
    val version: Int
)

@Serializable
data class PlanetProperties(
    val created: String,
    val edited: String,
    val climate: String,
    @SerialName("surface_water")
    val surfaceWater: String,
    val name: String,
    val diameter: String,
    @SerialName("rotation_period")
    val rotationPeriod: String,
    val terrain: String,
    val gravity: String,
    @SerialName("orbital_period")
    val orbitalPeriod: String,
    val population: String,
    val url: String
)

@Serializable
data class Support(
    val contact: String,
    val donate: String,
    @SerialName("partnerDiscounts")
    val partnerDiscounts: PartnerDiscounts
)

@Serializable
data class PartnerDiscounts(
    val saberMasters: Discount,
    val heartMath: Discount
)

@Serializable
data class Discount(
    val link: String,
    val details: String
)

@Serializable
data class Social(
    val discord: String,
    val reddit: String,
    val github: String
)