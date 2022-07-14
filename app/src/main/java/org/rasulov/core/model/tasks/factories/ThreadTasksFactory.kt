package org.rasulov.core.model.tasks.factories

import org.rasulov.core.model.tasks.AbstractTask
import org.rasulov.core.model.tasks.Callback
import org.rasulov.core.model.tasks.SynchronizedTask
import org.rasulov.core.model.tasks.Task


class ThreadTasksFactory : TaskFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(callback: Callback<T>) {
            thread = Thread {
                executeBody(body, callback)
            }.also { it.start() }
        }

        override fun doCancel() {
            thread?.interrupt()
        }
    }
}