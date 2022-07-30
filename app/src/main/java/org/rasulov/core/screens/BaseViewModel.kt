package org.rasulov.core.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnPending
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.model.Result
import org.rasulov.core.utils.MutableLiveResult


/**
 * Base class for all view-models.
 */
abstract class BaseViewModel(
) : ViewModel() {


    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope = CoroutineScope(coroutineContext)

    open fun onResult(result: Any) {}


    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) = viewModelScope.launch {
        liveResult.value = OnPending()
        try {
            liveResult.value = OnSuccess(block())
        } catch (e: Exception) {
            liveResult.value = OnError(e)
        }
    }

    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block: suspend () -> T) =
        viewModelScope.launch {
            try {
                stateFlow.value = OnSuccess(block())
            } catch (e: Exception) {
                stateFlow.value = OnError(e)
            }
        }

    fun <T> SavedStateHandle.mutableStateFlow(key: String, initValue: T): MutableStateFlow<T> {
        val savedStateHandle = this
        val mutableStateFlow = MutableStateFlow(savedStateHandle[key] ?: initValue)

        viewModelScope.launch {
            mutableStateFlow.collect {
                savedStateHandle[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<T>(key).asFlow().collect {
                mutableStateFlow.value = it
            }
        }
        return mutableStateFlow
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