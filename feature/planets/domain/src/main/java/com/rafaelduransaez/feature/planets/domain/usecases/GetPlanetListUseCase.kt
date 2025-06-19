package com.rafaelduransaez.feature.planets.domain.usecases

import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.SwapiUseCase
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.model.PlanetSummaryModel
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlanetListUseCase @Inject constructor(
    private val repository: PlanetRepository
): SwapiUseCase<Unit, List<PlanetSummaryModel>, PlanetError>() {

    override suspend fun execute(params: Unit): Flow<SwapiResult<List<PlanetSummaryModel>, PlanetError>> {
        return repository.getPlanets()
    }
}