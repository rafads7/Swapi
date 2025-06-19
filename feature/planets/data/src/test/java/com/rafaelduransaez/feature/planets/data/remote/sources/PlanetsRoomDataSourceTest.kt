package com.rafaelduransaez.feature.planets.data.remote.sources

import com.rafaelduransaez.core.database.dao.PlanetDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PlanetsRoomDataSourceTest {

    private lateinit var planetsDaoMock: PlanetDao
    private lateinit var dataSource: PlanetsRoomDataSource

    @Before
    fun setUp() {
        planetsDaoMock = mock()
        dataSource = PlanetsRoomDataSource(planetsDaoMock)
    }

    @Test
    fun `getLastUpdated SHOULD call dao's getLastUpdated AND return its value`() = runTest {
        val expectedTimestamp = System.currentTimeMillis()
        whenever(planetsDaoMock.getLastUpdated()).doReturn(expectedTimestamp)

        val actualTimestamp = dataSource.getLastUpdated()

        assertEquals(expectedTimestamp, actualTimestamp)
        verify(planetsDaoMock).getLastUpdated()
    }

    @Test
    fun `getAllPlanets SHOULD call dao's getAll AND return its flow`() = runTest {
        val planets = listOf(tatooine, alderaan, hoth)
        whenever(planetsDaoMock.getAll()).doReturn(flowOf(planets))

        val planetsFlow = dataSource.getAllPlanets()

        assertEquals(planets, planetsFlow.first())
        verify(planetsDaoMock).getAll()
    }

    @Test
    fun `getPlanetById SHOULD call dao's getPlanetById AND return its flow`() = runTest {
        val testUid = tatooine.uid
        whenever(planetsDaoMock.getPlanetById(testUid)).doReturn(flowOf(tatooine))

        val planetFlow = dataSource.getPlanetById(testUid)

        assertEquals(tatooine, planetFlow.first())
        verify(planetsDaoMock).getPlanetById(testUid)
    }

    @Test
    fun `savePlanet SHOULD return true WHEN dao insert returns a positive number`() = runTest {
        val planetToSave = tatooine
        whenever(planetsDaoMock.insert(planetToSave)).doReturn(1L)

        val result = dataSource.savePlanet(planetToSave)

        assertTrue(result)
        verify(planetsDaoMock).insert(planetToSave)
    }

    @Test
    fun `savePlanet SHOULD return false WHEN dao insert returns zero`() = runTest {
        val planetToSave = tatooine
        whenever(planetsDaoMock.insert(tatooine)).doReturn(0L)

        val result = dataSource.savePlanet(planetToSave)

        assertFalse(result)
        verify(planetsDaoMock).insert(planetToSave)
    }
}