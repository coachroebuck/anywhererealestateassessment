package com.sample.simpsonsviewer.main

import android.os.Bundle
import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMainViewModel(
    private val interactor: MainInteraction,
    private val coroutineScope: CoroutineScope,
) : MainViewModel {

    private var _query = ""
    private val _stateFlow: MutableStateFlow<MainViewModelStore.State> =
        MutableStateFlow(MainViewModelStore.State.Idle)

    override val stateFlow: StateFlow<MainViewModelStore.State> = _stateFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        restoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SearchQueryKey, _query)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        restoreInstanceState(savedInstanceState)
    }

    override fun emit(intent: MainViewModelStore.Intent) {
        coroutineScope.launch {
            when(intent) {
                is MainViewModelStore.Intent.OnSearchQueryChanged -> onOnSearchQueryChanged(intent)
                is MainViewModelStore.Intent.SubmitQueryText -> onSubmitQueryText(intent)
            }
        }
    }

    private fun onOnSearchQueryChanged(intent: MainViewModelStore.Intent.OnSearchQueryChanged) {
        _query = intent.query ?: ""
    }

    private fun onSubmitQueryText(intent: MainViewModelStore.Intent.SubmitQueryText) {
        interactor.emit(MainInteractionStore.Intent.Search(intent.query))
    }

    override val query: String
        get() = _query

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            _query = it.getString(SearchQueryKey, "")
        }
    }

    companion object {
        private val SearchQueryKey = "SearchQuery"
    }
}