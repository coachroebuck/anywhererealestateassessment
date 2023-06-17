package com.sample.simpsonsviewer.main.mvi

import com.sample.simpsonsviewer.model.ServiceResponse

sealed interface MainRepositoryStore {
    sealed interface Intent {
        data class Search(val query: String?): Intent
    }
    sealed interface State {
        object Idle : State
        object InProgress : State
        data class Success(val response: ServiceResponse? = null): State
        data class Error(val throwable: Throwable): State
    }
}