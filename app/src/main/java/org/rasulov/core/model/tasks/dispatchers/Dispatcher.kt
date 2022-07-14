package org.rasulov.core.model.tasks.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)
}