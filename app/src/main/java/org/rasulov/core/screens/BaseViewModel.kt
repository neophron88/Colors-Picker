package org.rasulov.core.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnPending
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.utils.MutableLiveResult


/**
 * Base class for all view-models.
 */
abstract class BaseViewModel(
) : ViewModel() {


    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope = CoroutineScope(coroutineContext)

    open fun onResult(result: Any) {}


    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        liveResult.value = OnPending()

        viewModelScope.launch {
            try {
                liveResult.value = OnSuccess(block())
            } catch (e: Exception) {
                liveResult.value = OnError(e)
            }
        }
    }


    private fun clear() {
        coroutineContext.cancel()
    }

    fun onBackPressed() {
        clear()
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }


}