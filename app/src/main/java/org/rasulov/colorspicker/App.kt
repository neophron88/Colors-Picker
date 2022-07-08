package org.rasulov.colorspicker

import org.rasulov.colorspicker.model.colors.InMemoryColorsRepository
import org.rasulov.core.BaseApplication
import org.rasulov.core.model.Repository

/**
 * Here we store instances of model layer classes.
 */
class App : BaseApplication() {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories = listOf<Repository>(
        InMemoryColorsRepository()
    )

}