package com.sample.simpsonsviewer.main.mvi

import com.sample.simpsonsviewer.model.ServiceResponseSummary

sealed interface MainInteractionStore {
    sealed interface Intent {
        data class Search(val query: String?): Intent
    }
    sealed interface State {
        object Idle : State
        object InProgress : State
        data class Success(val data: List<ServiceResponseSummary>): State
        data class Error(val throwable: Throwable): State
    }
}
