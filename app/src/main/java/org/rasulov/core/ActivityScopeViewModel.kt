package org.rasulov.core

import androidx.lifecycle.ViewModel
import org.rasulov.core.navigator.IntermediateNavigator
import org.rasulov.core.navigator.Navigator
import org.rasulov.core.uiactions.UiActions


class ActivityScopeViewModel(
    uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(), Navigator by navigator, UiActions by uiActions {


    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}