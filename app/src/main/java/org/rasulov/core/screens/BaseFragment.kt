package org.rasulov.core.screens

import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.rasulov.core.FragmentsHolder
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnPending
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.model.Result

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    fun notifyScreenUpdates() {
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()
    }

    fun <T> renderResult(
        parent: ViewGroup,
        result: Result<T>,
        onPending: () -> Unit,
        onError: () -> Unit,
        onSuccess: (T) -> Unit,
    ) {
        parent.children.forEach { it.isVisible = false }
        when (result) {
            is OnSuccess -> onSuccess(result.data)
            is OnPending -> onPending()
            is OnError -> onError()
        }

    }
}