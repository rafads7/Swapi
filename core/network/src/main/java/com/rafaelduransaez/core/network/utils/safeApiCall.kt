package com.rafaelduransaez.core.network.utils

import com.rafaelduransaez.core.base.common.SwapiFailure
import com.rafaelduransaez.core.base.common.SwapiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): SwapiResult<T, ApiError> = try {
    val response = call()
    if (response.isSuccessful) {
        response.body()?.let {
            SwapiResult.Success(it)
        } ?: SwapiResult.Failure(ApiError.EmptyBody)
    } else {
        SwapiResult.Failure(ApiError.Http(response.code(), response.message()))
    }
} catch (e: SocketTimeoutException) {
    SwapiResult.Failure(ApiError.Timeout)
} catch (e: IOException) {
    SwapiResult.Failure(ApiError.Network)
} catch (e: Exception) {
    SwapiResult.Failure(ApiError.Unknown(e))
}

inline fun <S> safeApiFlowCall(
    crossinline block: suspend () -> SwapiResult<S, SwapiFailure>
): Flow<SwapiResult<S, SwapiFailure>> = flow {
    try {
        emit(block())
    } catch (e: Exception) {
        emit(SwapiResult.failure(ApiError.Unknown(e)))
    }
}
