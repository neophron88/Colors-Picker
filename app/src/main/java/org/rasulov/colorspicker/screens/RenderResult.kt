package org.rasulov.colorspicker.screens

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import org.rasulov.colorspicker.databinding.PartResultBinding
import org.rasulov.core.model.OnSuccess
import org.rasulov.core.model.Result
import org.rasulov.core.screens.BaseFragment


fun <T> BaseFragment.usualRenderResult(
    parent: ViewGroup,
    result: Result<T>,
    onSuccess: (T) -> Unit
) {
    val binding: PartResultBinding = PartResultBinding.bind(parent)
    renderResult(
        parent,
        result,
        { binding.progressBar.isVisible = true },
        { binding.errorContainer.isVisible = true },
        { dataResult ->
            parent.children
                .filterNot { (it.id == binding.progressBar.id || it.id == binding.errorContainer.id) }
                .forEach { it.isVisible = true }
            onSuccess(dataResult)
        }
    )

}