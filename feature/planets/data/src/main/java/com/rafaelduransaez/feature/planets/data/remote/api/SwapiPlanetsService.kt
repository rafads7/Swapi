package com.rafaelduransaez.feature.planets.data.remote.api

import com.rafaelduransaez.feature.planets.data.remote.model.PlanetsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SwapiPlanetsService {

    companion object {
        const val ENDPOINT = "planets/"
    }

    @GET(ENDPOINT)
    suspend fun getPlanets(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("expanded") expanded: Boolean = true
    ): Response<PlanetsResponseDto>
}