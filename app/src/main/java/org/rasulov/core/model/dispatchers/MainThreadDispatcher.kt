package org.rasulov.core.model.dispatchers

import android.os.Handler
import android.os.Looper

class MainThreadDispatcher : Dispatcher {

    private val handler = Handler(Looper.getMainLooper())

    override fun dispatch(block: () -> Unit) {
        if (Thread.currentThread().id == Looper.getMainLooper().thread.id) {
            block.invoke()
        } else {
            handler.post(block)
        }
    }
}