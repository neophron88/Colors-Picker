package org.rasulov.core.model.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

    class Default : Dispatcher {
        override fun dispatch(block: () -> Unit) {
            block()
        }

    }
}