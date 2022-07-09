package org.rasulov.core.screens

import androidx.lifecycle.ViewModel
import org.rasulov.colorspicker.model.tasks.Callback
import org.rasulov.colorspicker.model.tasks.Task
import org.rasulov.core.model.OnPending
import org.rasulov.core.utils.MutableLiveResult


/**
 * Base class for all view-models.
 */
abstract class BaseViewModel : ViewModel() {

    private val tasks = mutableSetOf<Task<*>>()

    open fun onResult(result: Any) {
    }

    fun <T> Task<T>.safeEnqueue(listener: Callback<T>? = null) {
        tasks.add(this)
        this.enqueue {
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

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
        tasks.clear()
    }
}