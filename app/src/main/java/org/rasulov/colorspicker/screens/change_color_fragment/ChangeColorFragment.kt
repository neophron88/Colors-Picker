package org.rasulov.colorspicker.screens.change_color_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import org.rasulov.colorspicker.R
import org.rasulov.colorspicker.databinding.FragmentChangeColorBinding
import org.rasulov.colorspicker.databinding.PartResultBinding
import org.rasulov.colorspicker.screens.usualRenderResult
import org.rasulov.core.screens.HasScreenTitle
import org.rasulov.core.screens.BaseFragment
import org.rasulov.core.screens.BaseScreen
import org.rasulov.core.screens.screenViewModel
import kotlin.math.max
import kotlin.math.min

class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("it0088", "onCreate: f")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.viewState.observe(viewLifecycleOwner) { resultViewState ->
            with(binding) {
                usualRenderResult(root, resultViewState) {
                    adapter.items = it.colorsList
                    cancelButton.isVisible = it.showCancelButton
                    saveButton.isVisible = it.showSaveButton
                    saveProgressBar.isVisible = it.showSaveProgressBar

                }
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            notifyScreenUpdates()
        }

        val binding2 = PartResultBinding.bind(binding.root)
        binding2.btnTryAgain.setOnClickListener {
            viewModel.tryAgain()
        }
        Log.d("it0088", "onCreateView: f")
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.d("it0088", "onPause: f")
    }
    override fun onStop() {
        super.onStop()
        Log.d("it0088", "onStop: f")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("it0088", "onDestroyView: f")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("it0088", "onDestroy: f")
    }



    override fun getScreenTitle(): String? = viewModel.screenTitle.value



    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                //todo treeobserver
                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val minItemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / minItemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager =
                    GridLayoutManager(requireContext(), columns)
            }
        })
    }
}