package org.rasulov.core.screens

import org.rasulov.colorspicker.ActivityScopeViewModel

interface FragmentsHolder {

    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel
}