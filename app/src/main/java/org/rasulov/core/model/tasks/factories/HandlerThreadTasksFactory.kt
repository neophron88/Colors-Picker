package org.rasulov.core.model.tasks.factories

import android.os.Handler
import android.os.HandlerThread
import org.rasulov.core.model.tasks.AbstractTask
import org.rasulov.core.model.tasks.Callback
import org.rasulov.core.model.tasks.SynchronizedTask
import org.rasulov.core.model.tasks.Task
import org.rasulov.core.model.tasks.dispatchers.Dispatcher

class HandlerThreadTasksFactory : TaskFactory {

    private val handlerThread = HandlerThread(javaClass.name)

    init {
        handlerThread.start()
    }

    private val handler = Handler(handlerThread.looper)

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(HandlerThreadTask(body))
    }

    private inner class HandlerThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(callback: Callback<T>) {
            val runnable = Runnable {
                thread = Thread {
                    executeBody(body, callback)
                }
            }
            handler.post(runnable)
        }

        override fun doCancel() {
            thread?.interrupt()
        }
    }


}