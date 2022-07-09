package org.rasulov.colorspicker.model.tasks

import android.os.Handler
import android.os.Looper
import org.rasulov.core.model.FinalResult
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnSuccess
import java.lang.Exception

private val handler = Handler(Looper.getMainLooper())

class SimpleTasksFactory : TaskFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SimpleTask(body)
    }


    class SimpleTask<T>(
        private val body: TaskBody<T>
    ) : Task<T> {

        private var thread: Thread? = null
        private var canceled = false

        override fun await(): T {
            return body.invoke()
        }

        override fun enqueue(callback: Callback<T>) {
            thread = Thread {
                try {
                    val data = body.invoke()
                    publishResult(OnSuccess(data), callback)
                } catch (e: Exception) {
                    publishResult(OnError(e), callback)
                }
            }.also { it.start() }
        }

        private fun publishResult(result: FinalResult<T>, callback: Callback<T>) {
            handler.post {
                if (canceled) return@post
                callback.invoke(result)
            }
        }

        override fun cancel() {
            canceled = true
            thread = null
        }

    }
}