package org.rasulov.core.model.tasks.factories

import org.rasulov.core.model.tasks.Task

typealias TaskBody<T> = () -> T

interface TaskFactory  {

    fun <T> async(body: TaskBody<T>): Task<T>
}