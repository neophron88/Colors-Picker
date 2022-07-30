package org.rasulov.colorspicker.screens.change_color_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.model.colors.ColorsRepository
import org.rasulov.colorspicker.model.entity.NamedColor
import org.rasulov.core.model.*
import org.rasulov.core.navigator.Navigator
import org.rasulov.core.screens.BaseViewModel
import org.rasulov.core.uiactions.UiActions


class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel(), ColorsAdapter.Listener {

    val a = MutableSharedFlow<String>().buffer()
    // input sources
    private val _availableColors = MutableStateFlow<Result<List<NamedColor>>>(OnPending())
    private val _currentColorId =
        savedStateHandle.mutableStateFlow("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableStateFlow<Progress>(NotInProgress)

    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
        ::mergeSources
    )

    val screenTitle: LiveData<String> =
        viewState
            .map { result ->
                val s = if (result is OnSuccess) {
                    val name =
                        result.data.colorsList.first { item -> item.selected }.namedColor.name
                    uiActions.getString(R.string.change_color_screen_title, name)
                } else uiActions.getString(R.string.change_color)
                s
            }.buffer()
            .asLiveData()

    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _saveInProgress.value = InProgress.START
            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor).collect {
                _saveInProgress.value = InProgress(it)
            }
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            uiActions.toast(uiActions.getString(R.string.error_please_try_again))
        } finally {
            _saveInProgress.value = NotInProgress
        }
    }

    fun onCancelPressed() {
        navigator.goBack()

    }


    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        saveInProgress: Progress
    ): Result<ViewState> {

        return colors.map {
            ViewState(
                it.map { color -> NamedColorListItem(color, currentColorId == color.id) },
                !saveInProgress.isInProgress(),
                !saveInProgress.isInProgress(),
                saveInProgress.isInProgress(),
                saveInProgress.getPercent(),
                uiActions.getString(
                    R.string.percent,
                    saveInProgress.getPercent()
                )
            )

        }

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
        val showSaveProgressBar: Boolean,
        val saveProgressPercent: Int,
        val saveProgressPercentMessage: String
    )

}