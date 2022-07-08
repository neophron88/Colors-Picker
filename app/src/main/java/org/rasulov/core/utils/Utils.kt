package org.rasulov.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.rasulov.core.model.Result

const val ARG_SCREEN = "ARG_SCREEN"

typealias ResourceAction<T> = (T) -> Unit

typealias ViewModelCreator = () -> ViewModel?


typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>





