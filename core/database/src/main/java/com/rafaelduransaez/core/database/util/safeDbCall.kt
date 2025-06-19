package com.rafaelduransaez.core.database.util

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.rafaelduransaez.core.base.common.SwapiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeDbCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    dbCall: suspend () -> T
): SwapiResult<T, DatabaseError> = withContext(dispatcher) {
    try {
        SwapiResult.success(dbCall())
    } catch (e: Exception) {
        SwapiResult.failure(e.toDatabaseError())
    }
}

inline fun <reified T> safeDbFlow(
    crossinline block: () -> Flow<T?>
): Flow<SwapiResult<T, DatabaseError>> {
    return block()
        .map<T?, SwapiResult<T, DatabaseError>> { result ->
            when {
                result == null -> SwapiResult.Failure(DatabaseError.NotFound)
                result is List<*> && result.isEmpty() ->
                    SwapiResult.Failure(DatabaseError.NotFound)

                else -> SwapiResult.Success(result)
            }
        }
        .catch { e ->
            if (e is CancellationException) throw e
            emit(SwapiResult.Failure(e.toDatabaseError()))
        }
}


fun Throwable.toDatabaseError(): DatabaseError = when (this) {
    is SQLiteConstraintException,
    is SQLiteDatabaseCorruptException,
    is SQLiteDiskIOException,
    is TimeoutCancellationException,
    is SQLiteFullException,
    is SQLiteException -> {
        if (this.message?.contains("syntax", ignoreCase = true) == true) {
            DatabaseError.Unknown(this)
        } else {
            DatabaseError.Unknown(this)
        }
    }
    else -> DatabaseError.Unknown(this)
}
