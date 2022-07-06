package org.rasulov.colorspicker.screens.change_color_fragment

import org.rasulov.colorspicker.model.entity.NamedColor

/**
 * Represents list item for the color; it may be selected or not
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)