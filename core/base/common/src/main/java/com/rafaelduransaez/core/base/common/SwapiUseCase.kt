package com.rafaelduransaez.core.base.common

import kotlinx.coroutines.flow.Flow

@Suppress("UNCHECKED_CAST")
abstract class SwapiUseCase<in Params, out Result, out Failure : SwapiFailure> {

    suspend operator fun invoke(params: Params = Unit as Params): Flow<SwapiResult<Result, Failure>> =
        execute(params)

    protected abstract suspend fun execute(params: Params): Flow<SwapiResult<Result, Failure>>

}