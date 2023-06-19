package com.sample.simpsonsviewer.main.mvi

import com.sample.simpsonsviewer.model.ServiceResponseSummary
import com.sample.simpsonsviewer.model.ServiceResponseSummaryList

sealed interface MainViewModelStore {
    sealed interface Intent {
        object RetrieveInformation : Intent

        data class OnSearchQueryChanged(val query: String?): Intent
        data class SubmitQueryText(val query: String?): Intent
    }
    sealed interface State {
        object Idle : State
        object InProgress : State

        data class OriginalSearchQuery(val query: String): State
        data class Response(val data: ServiceResponseSummaryList) : State
        data class Error(val throwable: Throwable) : State
    }
}