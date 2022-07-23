package org.rasulov.colorspicker

import org.rasulov.colorspicker.model.colors.InMemoryColorsRepository
import org.rasulov.core.BaseApplication
import org.rasulov.core.model.dispatchers.MainThreadDispatcher

class App : BaseApplication() {


    override val singletonScopeDependencies = listOf(
        InMemoryColorsRepository(),
    )

}