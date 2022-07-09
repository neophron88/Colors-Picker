package org.rasulov.colorspicker.model.tasks

import org.rasulov.core.model.FinalResult

typealias Callback<T> = (FinalResult<T>) -> Unit

interface Task<T> {


    fun await(): T

    fun enqueue(callback: Callback<T>)

    fun cancel()
}