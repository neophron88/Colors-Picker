package org.rasulov.colorspicker.screens.current_color_fragment

import android.util.Log
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.model.colors.ColorListener
import org.rasulov.colorspicker.model.colors.ColorsRepository
import org.rasulov.colorspicker.model.entity.NamedColor
import org.rasulov.core.model.tasks.dispatchers.Dispatcher
import org.rasulov.colorspicker.screens.change_color_fragment.ChangeColorFragment
import org.rasulov.core.model.OnPending
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.model.takeSuccess
import org.rasulov.core.navigator.Navigator
import org.rasulov.core.screens.BaseViewModel
import org.rasulov.core.uiactions.UiActions
import org.rasulov.core.utils.LiveResult
import org.rasulov.core.utils.MutableLiveResult

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(OnPending())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(OnSuccess(it))
    }

    init {
        colorsRepository.addListener(colorListener)
        load()
    }


    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }


    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() {
        load()
    }


    private fun load() {
        colorsRepository.getCurrentColor().into(_currentColor)
    }


    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
        Log.d("it0088", "onCleared: ")
    }

}
