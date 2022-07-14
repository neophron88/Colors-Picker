package org.rasulov.core.model.tasks

import org.rasulov.core.model.tasks.dispatchers.Dispatcher
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

class SynchronizedTask<T>(
    private val task: Task<T>
) : Task<T> {

    @Volatile
    private var cancelled = false
    private var launched = false
    private val listenerCalled = AtomicBoolean(false)

    override fun await(): T {
        synchronized(this) {
            if (cancelled) throw CancelledException()
            if (launched) throw IllegalStateException("Task is already launched")
            launched = true
        }

        return task.await()

    }

    override fun enqueue(dispatcher: Dispatcher, callback: Callback<T>) = synchronized(this) {
        if (cancelled) return
        if (launched) throw IllegalStateException("Task is already launched")
        launched = true
        task.enqueue(dispatcher) {
            if (listenerCalled.compareAndSet(false, true)) {
                if (!cancelled) callback.invoke(it)
            }
        }
    }

    override fun cancel() = synchronized(this) {
        if (listenerCalled.compareAndSet(false, true)) {
            if (cancelled) return
            cancelled = true
            task.cancel()
        }
    }
}