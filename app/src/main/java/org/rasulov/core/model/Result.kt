package org.rasulov.core.model

sealed class Result<T> {
    inline fun <R> map(transfer: (T) -> R): Result<R> = when (this) {
        is OnPending -> OnPending()
        is OnSuccess -> OnSuccess(transfer(data))
        is OnError -> OnError(error)
    }

}

class OnPending<T> : Result<T>()

class OnSuccess<T>(val data: T) : Result<T>()

class OnError<T>(val error: Exception) : Result<T>()

fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is OnSuccess<T>) data else null
}

