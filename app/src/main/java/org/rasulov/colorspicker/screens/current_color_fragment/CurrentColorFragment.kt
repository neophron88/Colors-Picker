package org.rasulov.colorspicker.screens.current_color_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.rasulov.colorspicker.databinding.FragmentCurrentColorBinding
import org.rasulov.colorspicker.databinding.PartResultBinding
import org.rasulov.colorspicker.screens.usualRenderResult
import org.rasulov.core.model.OnError
import org.rasulov.core.model.OnPending
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.screens.BaseFragment
import org.rasulov.core.screens.BaseScreen
import org.rasulov.core.screens.screenViewModel

class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) { result ->

            Log.d("it0088", "observe: $result")
            usualRenderResult(binding.root, result) {
                binding.colorView.setBackgroundColor(it.value)
            }

            binding.changeColorButton.setOnClickListener {
                viewModel.changeColor()
            }

            val binding2 = PartResultBinding.bind(binding.root)
            binding2.btnTryAgain.setOnClickListener {
                viewModel.tryAgain()
            }
        }

        return binding.root

    }
}