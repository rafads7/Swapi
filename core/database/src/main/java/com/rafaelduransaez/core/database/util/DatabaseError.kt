package com.rafaelduransaez.core.database.util

import com.rafaelduransaez.core.base.common.SwapiFailure

sealed interface DatabaseError: SwapiFailure {
    data class DatabaseOperationsError(val throwable: Throwable) : DatabaseError
    data class SqlSyntaxError(val throwable: Throwable) : DatabaseError
    data object NotFound : DatabaseError
    data class Unknown(val throwable: Throwable) : DatabaseError
}