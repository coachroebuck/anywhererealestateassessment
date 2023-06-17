package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import kotlinx.coroutines.flow.StateFlow

interface MainInteraction {
    fun emit(intent: MainInteractionStore.Intent)

    val stateFlow: StateFlow<MainInteractionStore.State>
}