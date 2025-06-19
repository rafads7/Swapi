package com.rafaelduransaez.feature.planets.presentation.list

import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapperImpl
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.presentation.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class PlanetListUiMapperImplTest {

    private lateinit var mapper: PlanetListUiMapper

    @Before
    fun setUp() {
        mapper = PlanetListUiMapperImpl()
    }

    @Test
    fun `getErrorMessageId given DatabaseNotFoundError returns correct database not found resource ID`() {
        val error = PlanetError.DatabaseNotFoundError

        val resultId = mapper.getErrorMessageId(error)

        val expectedId = R.string.planets_error_database_not_found
        assertEquals(expectedId, resultId)
    }

    @Test
    fun `getErrorMessageId given DatabaseKindError returns correct database unknown resource ID`() {
        val error = PlanetError.DatabaseKindError

        val resultId = mapper.getErrorMessageId(error)

        val expectedId = R.string.planets_error_database_unknown
        assertEquals(expectedId, resultId)
    }

    @Test
    fun `getErrorMessageId given ServerError returns correct server error resource ID`() {
        val error = PlanetError.ServerError

        val resultId = mapper.getErrorMessageId(error)

        val expectedId = R.string.planets_error_server
        assertEquals(expectedId, resultId)
    }

    @Test
    fun `getErrorMessageId given Unknown returns correct unknown error resource ID`() {
        val error = PlanetError.Unknown

        val resultId = mapper.getErrorMessageId(error)

        val expectedId = R.string.planets_error_unknown
        assertEquals(expectedId, resultId)
    }
}