package com.rafaelduransaez.feature.planets.domain.usecases

import com.rafaelduransaez.core.base.common.SwapiResult
import com.rafaelduransaez.core.base.common.SwapiUseCase
import com.rafaelduransaez.feature.planets.domain.model.PlanetDetailModel
import com.rafaelduransaez.feature.planets.domain.model.PlanetError
import com.rafaelduransaez.feature.planets.domain.repository.PlanetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlanetDetailUseCase @Inject constructor(
    private val repository: PlanetRepository
) : SwapiUseCase<String, PlanetDetailModel, PlanetError>() {

    override suspend fun execute(params: String): Flow<SwapiResult<PlanetDetailModel, PlanetError>> {
        return repository.getPlanetById(params)
    }
}