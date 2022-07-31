package org.rasulov.colorspicker.screens.current_color_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("it0088", "onCreate: fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
        Log.d("it0088", "onCreateView: fragment")
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->

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

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.sharedFlow.collect {
//                delay(100)
//                Log.d("itrt0088", "sharedFlow collect: $it")
//            }
//        }

        return binding.root

    }

    override fun onPause() {
        super.onPause()
        Log.d("it0088", "onPause: fragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("it0088", "onDestroy: fragment")
    }
}