package org.rasulov.colorspicker

import android.app.Application
import org.rasulov.colorspicker.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application() {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    val models = listOf<Any>(
        InMemoryColorsRepository()
    )

}