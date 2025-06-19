package com.rafaelduransaez.core.network.utils

import com.rafaelduransaez.core.base.common.SwapiFailure

sealed class ApiError: SwapiFailure {
    data object Network : ApiError()
    data object Timeout : ApiError()
    data class Http(val code: Int, val message: String?) : ApiError()
    data object EmptyBody : ApiError()
    data class Unknown(val throwable: Throwable) : ApiError()
}

