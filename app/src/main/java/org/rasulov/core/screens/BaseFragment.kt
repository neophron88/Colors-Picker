package org.rasulov.core.screens

import androidx.fragment.app.Fragment
import org.rasulov.colorspicker.MainActivity

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    fun notifyScreenUpdates() {
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()
    }
}