package org.rasulov.colorspicker

import org.rasulov.colorspicker.model.colors.InMemoryColorsRepository
import org.rasulov.colorspicker.model.tasks.SimpleTasksFactory
import org.rasulov.colorspicker.model.tasks.TaskFactory
import org.rasulov.core.BaseApplication
import org.rasulov.core.model.Repository

class App : BaseApplication() {

    private val factory = SimpleTasksFactory()
    override val repositories = listOf(
        InMemoryColorsRepository(factory),
        factory
    )

}