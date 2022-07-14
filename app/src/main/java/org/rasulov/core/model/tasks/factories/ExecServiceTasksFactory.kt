package org.rasulov.core.model.tasks.factories

import org.rasulov.core.model.tasks.AbstractTask
import org.rasulov.core.model.tasks.Callback
import org.rasulov.core.model.tasks.SynchronizedTask
import org.rasulov.core.model.tasks.Task
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class ExecServiceTasksFactory(
    private val service: ExecutorService
) : TaskFactory {
    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecServiceTask(body))
    }

    private inner class ExecServiceTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(callback: Callback<T>) {

            future = service.submit {
                return@submit executeBody(body, callback)
            }
        }

        override fun doCancel() {
            future?.cancel(true)
        }
    }
}