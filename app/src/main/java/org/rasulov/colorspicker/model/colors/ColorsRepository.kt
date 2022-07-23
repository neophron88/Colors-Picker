package org.rasulov.colorspicker.model.colors

import org.rasulov.core.model.Repository
import org.rasulov.colorspicker.model.entity.NamedColor


typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    suspend fun getAvailableColors(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    suspend fun getCurrentColor(): NamedColor

    suspend fun setCurrentColor(color: NamedColor)

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)

}