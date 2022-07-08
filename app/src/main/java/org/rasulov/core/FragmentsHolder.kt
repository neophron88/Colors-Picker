package org.rasulov.core


interface FragmentsHolder {

    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel
}