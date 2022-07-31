package org.rasulov.core.model

sealed class Result<T> {
    inline fun <R> map(transfer: (T) -> R): Result<R> = when (this) {
        is OnPending -> OnPending()
        is OnSuccess -> OnSuccess(transfer(data))
        is OnError -> OnError(error)
    }
}

sealed class FinalResult<T> : Result<T>()

class OnPending<T> : Result<T>()

class OnSuccess<T>(val data: T) : FinalResult<T>()

class OnError<T>(val error: Exception) : FinalResult<T>()

fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is OnSuccess<T>) data else null
}

