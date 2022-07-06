package org.rasulov.colorspicker.screens.current_color_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.model.colors.ColorListener
import org.rasulov.colorspicker.model.colors.ColorsRepository
import org.rasulov.colorspicker.model.entity.NamedColor
import org.rasulov.core.navigator.Navigator
import org.rasulov.core.uiactions.UiActions
import org.rasulov.core.screens.BaseViewModel
import org.rasulov.colorspicker.screens.change_color_fragment.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}