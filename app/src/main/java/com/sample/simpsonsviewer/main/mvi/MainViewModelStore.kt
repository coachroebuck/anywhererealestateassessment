package com.sample.simpsonsviewer.main.mvi

sealed interface MainViewModelStore {
    sealed interface Intent {
        data class OnSearchQueryChanged(val query: String?): Intent
        data class SubmitQueryText(val query: String?): Intent
    }
    sealed interface State {
        object Idle : State
        data class OriginalSearchQuery(val query: String): State
    }
}