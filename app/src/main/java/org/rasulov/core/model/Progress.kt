package org.rasulov.core.model

sealed class Progress

object NotInProgress : Progress()

data class InProgress(val percent: Int) : Progress() {

    companion object {
        val START = InProgress(0)
    }
}


fun Progress.isInProgress() = this !is NotInProgress

fun Progress.getPercent() = (this as? InProgress)?.percent ?: InProgress.START.percent

