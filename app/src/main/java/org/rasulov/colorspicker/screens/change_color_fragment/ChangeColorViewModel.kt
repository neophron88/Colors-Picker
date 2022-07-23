package org.rasulov.colorspicker.screens.change_color_fragment

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.model.colors.ColorsRepository
import org.rasulov.colorspicker.model.entity.NamedColor
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.navigator.Navigator
import org.rasulov.core.screens.BaseViewModel
import org.rasulov.core.uiactions.UiActions
import org.rasulov.core.utils.LiveResult
import org.rasulov.core.utils.MediatorLiveResult
import org.rasulov.core.utils.MutableLiveResult


class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableLiveResult<List<NamedColor>>()
    private val _currentColorId =
        savedStateHandle.getLiveData("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState


    val screenTitle: LiveData<String> =
        Transformations.map(_viewState) { result ->
            val s = if (result is OnSuccess) {
                val name = result.data.colorsList.first { item -> item.selected }.namedColor.name
                uiActions.getString(R.string.change_color_screen_title, name)
            } else uiActions.getString(R.string.change_color)
            s

        }

    init {
        load()
        _viewState.addSource(_availableColors) { mergeSources() }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _saveInProgress.value = true
            val currentColorId = _currentColorId.value
                ?: throw IllegalArgumentException("Current colorID always needs a value")
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor)
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            uiActions.toast(uiActions.getString(R.string.error_please_try_again))
        } finally {
            _saveInProgress.value = false
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }


    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return
        val result = colors.map {

            ViewState(
                it.map { color -> NamedColorListItem(color, currentColorId == color.id) },
                !saveInProgress,
                !saveInProgress,
                saveInProgress
            )

        }
        _viewState.value = result

    }

    fun tryAgain() {
        load()
    }

    private fun load() {
        into(_availableColors) { colorsRepository.getAvailableColors() }
    }


    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean
    )

}