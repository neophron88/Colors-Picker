package org.rasulov.colorspicker

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testFlows() {

        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
            .filter {
                println("$it filter")
                it % 2 == 0
            }
            .map {
                println("$it map")
                it * 10
            }.asFlow()
        println("-----------------------------------------------------------")
        runBlocking {
            val flow: Flow<Int> = flowOf(1, 2, 4, 5, 6, 7, 8, 9)
            list.filter {
                println("$it filter")
                it % 2 == 0
            }
                .map {
                    println("$it map")
                    it * 10
                }
                .collect {
                    println(it)
                }
        }
    }
}