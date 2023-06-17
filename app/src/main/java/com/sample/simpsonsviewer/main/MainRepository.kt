package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    val stateFlow: StateFlow<MainRepositoryStore.State>
    fun emit(intent: MainRepositoryStore.Intent)
}