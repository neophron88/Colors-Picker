package org.rasulov.colorspicker.model.tasks

import org.rasulov.core.model.Repository

typealias TaskBody<T> = () -> T

interface TaskFactory : Repository {

    fun <T> async(body: TaskBody<T>): Task<T>
}