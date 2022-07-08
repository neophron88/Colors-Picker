package org.rasulov.core.screens

import androidx.lifecycle.ViewModel


/**
 * Base class for all view-models.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {
    }

}