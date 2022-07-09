package org.rasulov.colorspicker.model.colors

import org.rasulov.core.model.Repository
import org.rasulov.colorspicker.model.entity.NamedColor
import org.rasulov.colorspicker.model.tasks.Task


typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    fun getAvailableColors(): Task<List<NamedColor>>

    fun getById(id: Long): Task<NamedColor>

    fun getCurrentColor(): Task<NamedColor>

    fun setCurrentColor(color: NamedColor): Task<Unit>

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)

}