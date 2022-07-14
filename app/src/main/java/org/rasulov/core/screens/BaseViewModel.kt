package org.rasulov.core.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import org.rasulov.core.model.tasks.Callback
import org.rasulov.core.model.tasks.Task
import org.rasulov.core.model.tasks.dispatchers.Dispatcher
import org.rasulov.core.model.OnPending
import org.rasulov.core.utils.MutableLiveResult


/**
 * Base class for all view-models.
 */
abstract class BaseViewModel(
    private val dispatcher: Dispatcher
) : ViewModel() {

    private val tasks = mutableSetOf<Task<*>>()

    open fun onResult(result: Any) {
    }


    fun <T> Task<T>.safeEnqueue(listener: Callback<T>? = null) {
        tasks.add(this)
        this.enqueue(dispatcher) {
            tasks.remove(this)
            listener?.invoke(it)
        }
    }

    fun <T> Task<T>.into(liveResult: MutableLiveResult<T>) {
        liveResult.value = OnPending()
        this.safeEnqueue {
            liveResult.value = it
        }
    }

    fun onBackPressed() {
        clear()
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }

    private fun clear() {
        tasks.forEach { it.cancel() }
        tasks.clear()
    }


}