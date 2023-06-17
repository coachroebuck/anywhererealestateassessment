package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultMainInteraction(private val repository: MainRepository) : MainInteraction {
    private val _stateFlow: MutableStateFlow<MainInteractionStore.State> =
        MutableStateFlow(MainInteractionStore.State.Idle)

    override val stateFlow: StateFlow<MainInteractionStore.State> = _stateFlow

    override fun emit(intent: MainInteractionStore.Intent) {
        when(intent) {
            is MainInteractionStore.Intent.Search -> onSearch(intent)
        }
    }

    private fun onSearch(intent: MainInteractionStore.Intent.Search) {
        repository.emit(MainRepositoryStore.Intent.Search(intent.query))
    }

}