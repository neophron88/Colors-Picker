package org.rasulov.colorspicker.model.colors

import kotlinx.coroutines.flow.Flow
import org.rasulov.core.model.Repository
import org.rasulov.colorspicker.model.entity.NamedColor


typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    suspend fun getAvailableColors(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    suspend fun getCurrentColor(): NamedColor

    fun setCurrentColor(color: NamedColor): Flow<Int>

    fun listenCurrentColor(): Flow<NamedColor>

}