package org.rasulov.core.model.tasks

import org.rasulov.core.model.tasks.dispatchers.Dispatcher
import org.rasulov.core.model.FinalResult
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.model.tasks.factories.TaskBody

abstract class AbstractTask<T> : Task<T> {

    private var finalResult by Await<FinalResult<T>>()


    final override fun await(): T {
        val wrapperListener: Callback<T> = {
            finalResult = it
        }
        doEnqueue(wrapperListener)
        try {
            when (val result = finalResult) {
                is OnError -> throw result.error
                is OnSuccess -> return result.data
            }
        } catch (e: Exception) {
            if (e is InterruptedException) {
                cancel()
                throw CancelledException(e)
            } else {
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, callback: Callback<T>) {
        val wrappedListener: Callback<T> = {
            finalResult = it
            dispatcher.dispatch {
                callback(finalResult)
            }
        }
        doEnqueue(wrappedListener)
    }

    final override fun cancel() {
        finalResult = OnError(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, callback: Callback<T>) {
        try {
            val data = taskBody()
            callback(OnSuccess(data))
        } catch (e: Exception) {
            callback(OnError(e))
        }
    }

    abstract fun doEnqueue(callback: Callback<T>)

    abstract fun doCancel()

}