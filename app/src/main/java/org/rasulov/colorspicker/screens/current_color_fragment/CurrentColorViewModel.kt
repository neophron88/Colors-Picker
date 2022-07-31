package org.rasulov.colorspicker.screens.current_color_fragment

import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.model.colors.ColorListener
import org.rasulov.colorspicker.model.colors.ColorsRepository
import org.rasulov.colorspicker.model.entity.NamedColor
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
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(OnPending())
    val currentColor: LiveResult<NamedColor> = _currentColor

    val sharedFlow = MutableSharedFlow<Int>(
        5,
        10,
        BufferOverflow.SUSPEND
    )

    init {

        viewModelScope.launch {
//            repeat(100) {
//                Log.d("itrt0088", "repeat $it")
//                delay(10)
//                sharedFlow.emit(it)
//            }

            colorsRepository.listenCurrentColor().collect {
                _currentColor.value = OnSuccess(it)
            }
        }

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
        into(_currentColor) {
            colorsRepository.getCurrentColor()
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("it0088", "onCleared: ")
    }

}
