package org.rasulov.core.model.tasks

interface ThreadUtils {

    fun sleep(minutes: Int)

    class Default : ThreadUtils {
        override fun sleep(minutes: Int) {
            Thread.sleep((minutes * 1000).toLong())
        }
    }
}