package org.rasulov.core

import android.app.Application
import org.rasulov.core.model.Repository

abstract class BaseApplication : Application() {

    abstract val singletonScopeDependencies: List<Any>
}