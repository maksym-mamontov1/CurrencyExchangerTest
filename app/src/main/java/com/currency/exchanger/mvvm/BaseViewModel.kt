package com.currency.exchanger.mvvm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State> : ViewModel(), CoroutineScope {

    private val mutableStateFlow = MutableStateFlow(getInitialState())

    val stateFlow: StateFlow<State?>
        get() = mutableStateFlow.asStateFlow()

    var state
        get() = mutableStateFlow.value
        set(value) {
            mutableStateFlow.value = value
        }

    override val coroutineContext: CoroutineContext = Dispatchers.Default +
            SupervisorJob() +
            CoroutineExceptionHandler { context, throwable ->
                onError(throwable, context)
            }

    protected inline fun changeState(stateChange:  State.() -> State) {
        state = state.stateChange()
    }

    abstract fun getInitialState(): State

    open fun onError(throwable: Throwable, coroutineContext: CoroutineContext? = null) {
        if (throwable is CancellationException) {
            Timber.e(throwable, "Coroutine $coroutineContext cancellation exception")
        } else throw throwable
    }
}