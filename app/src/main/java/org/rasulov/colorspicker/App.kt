package org.rasulov.colorspicker

import org.rasulov.colorspicker.model.colors.InMemoryColorsRepository
import org.rasulov.core.BaseApplication
import org.rasulov.core.model.tasks.ThreadUtils
import org.rasulov.core.model.tasks.dispatchers.MainThreadDispatcher
import org.rasulov.core.model.tasks.factories.ExecServiceTasksFactory
import org.rasulov.core.model.tasks.factories.ThreadTasksFactory
import java.util.concurrent.Executors

class App : BaseApplication() {

    private val factory = ExecServiceTasksFactory(Executors.newCachedThreadPool())
    private val utils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()


    override val singletonScopeDependencies = listOf(
        InMemoryColorsRepository(factory, utils),
        factory,
        dispatcher
    )

}