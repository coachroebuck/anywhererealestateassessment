package com.sample.simpsonsviewer.main

import android.os.Bundle
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import kotlinx.coroutines.flow.StateFlow

interface MainViewModel {
    fun onCreate(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
    fun onRestoreInstanceState(savedInstanceState: Bundle)
    fun emit(intent: MainViewModelStore.Intent)

    val stateFlow: StateFlow<MainViewModelStore.State>
}