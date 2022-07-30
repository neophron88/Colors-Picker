package org.rasulov.colorspicker.model.colors

import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.rasulov.colorspicker.model.entity.NamedColor

class InMemoryColorsRepository : ColorsRepository {

    private var currentColor = AVAILABLE_COLORS[0]

    private val listeners = mutableSetOf<ColorListener>()

    override fun listenCurrentColor(): Flow<NamedColor> {
        return callbackFlow {
            val listener: ColorListener = {
                trySend(it)
            }
            listeners.add(listener)

            awaitClose {
                listeners.remove(listener)
            }
        }.buffer(Channel.CONFLATED)


    }


    override suspend fun getAvailableColors(): List<NamedColor> {
        return withContext(Dispatchers.IO) {
            delay(1500)
            AVAILABLE_COLORS
        }
    }


    override suspend fun getById(id: Long): NamedColor {
        return withContext(Dispatchers.IO) {
            AVAILABLE_COLORS.first { it.id == id }
        }
    }

    override suspend fun getCurrentColor(): NamedColor {
        return withContext(Dispatchers.IO) {
            delay(1500)
            currentColor
        }
    }

    override fun setCurrentColor(color: NamedColor): Flow<Int> {
        return flow {
            if (currentColor != color) {
                var progress = 0
                while (progress < 100) {
                    progress += 2
                    delay(50)
                    emit(progress)
                }
                currentColor = color
                notifyListeners()
            } else emit(100)
        }.buffer(Channel.CONFLATED)
    }

    private fun notifyListeners() {
        listeners.forEach { it(currentColor) }
    }

    companion object {
        private val AVAILABLE_COLORS = listOf(
            NamedColor(1, "Red", Color.RED),
            NamedColor(2, "Green", Color.GREEN),
            NamedColor(3, "Blue", Color.BLUE),
            NamedColor(4, "Yellow", Color.YELLOW),
            NamedColor(5, "Magenta", Color.MAGENTA),
            NamedColor(6, "Cyan", Color.CYAN),
            NamedColor(7, "Gray", Color.GRAY),
            NamedColor(8, "Navy", Color.rgb(0, 0, 128)),
            NamedColor(9, "Pink", Color.rgb(255, 20, 147)),
            NamedColor(10, "Sienna", Color.rgb(160, 82, 45)),
            NamedColor(11, "Khaki", Color.rgb(240, 230, 140)),
            NamedColor(12, "Forest Green", Color.rgb(34, 139, 34)),
            NamedColor(13, "Sky", Color.rgb(135, 206, 250)),
            NamedColor(14, "Olive", Color.rgb(107, 142, 35)),
            NamedColor(15, "Violet", Color.rgb(148, 0, 211)),
        )
    }
}