package org.rasulov.core.model.tasks

import org.rasulov.core.model.tasks.dispatchers.Dispatcher
import org.rasulov.core.model.FinalResult
import java.lang.Exception

typealias Callback<T> = (FinalResult<T>) -> Unit

class CancelledException(exception: Exception? = null) : Exception(exception) {

}

interface Task<T> {


    fun await(): T

    fun enqueue(dispatcher: Dispatcher, callback: Callback<T>)

    fun cancel()
}