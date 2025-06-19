package com.rafaelduransaez.core.base.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SwapiFailure

sealed class SwapiResult<out S, out F : SwapiFailure> {
    data class Success<out S>(val data: S) : SwapiResult<S, Nothing>()
    data class Failure<out F : SwapiFailure>(val error: F) : SwapiResult<Nothing, F>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getOrNull(): S? = (this as? Success)?.data
    fun errorOrNull(): F? = (this as? Failure)?.error

    inline fun fold(
        onSuccess: (S) -> Unit,
        onFailure: (F) -> Unit
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Failure -> onFailure(error)
        }
    }

    companion object {
        fun <T> success(value: T): Success<T> {
            return Success(value)
        }

        fun <T : SwapiFailure> failure(value: T): Failure<T> {
            return Failure(value)
        }
    }
}

inline fun <S, F : SwapiFailure, R> SwapiResult<S, F>.mapSuccess(transform: (S) -> R): SwapiResult<R, F> {
    return when (this) {
        is SwapiResult.Success -> SwapiResult.Success(transform(data))
        is SwapiResult.Failure -> this
    }
}

inline fun <S, F : SwapiFailure, F2 : SwapiFailure> SwapiResult<S, F>.mapFailure(
    transform: (F) -> F2
): SwapiResult<S, F2> {
    return when (this) {
        is SwapiResult.Success -> this
        is SwapiResult.Failure -> SwapiResult.Failure(transform(error))
    }
}

inline fun <S, F : SwapiFailure, R : SwapiFailure> Flow<SwapiResult<S, F>>.mapFailure(
    crossinline transform: (F) -> R
): Flow<SwapiResult<S, R>> {
    return map { result ->
        when (result) {
            is SwapiResult.Success -> SwapiResult.Success(result.data)
            is SwapiResult.Failure -> SwapiResult.Failure(transform(result.error))
        }
    }
}

inline fun <S, F : SwapiFailure, R> Flow<SwapiResult<S, F>>.mapSuccess(
    crossinline transform: suspend (S) -> R
): Flow<SwapiResult<R, F>> {
    return map { result ->
        when (result) {
            is SwapiResult.Success -> SwapiResult.Success(transform(result.data))
            is SwapiResult.Failure -> result
        }
    }
}

