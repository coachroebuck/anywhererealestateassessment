package com.sample.simpsonsviewer.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import com.sample.simpsonsviewer.model.ServiceResponseSummaryList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMainViewModel(
    private val interaction: MainInteraction,
    private val coroutineScope: CoroutineScope,
    private val sdkVersion: Int = Build.VERSION.SDK_INT,
    private val tiramisuVersion: Int = Build.VERSION_CODES.TIRAMISU,
) : MainViewModel {

    private var response: MainInteractionStore.State.Success? = null
    private var _query = ""
    private var selectedTitle = ""
    private val _stateFlow: MutableStateFlow<MainViewModelStore.State> =
        MutableStateFlow(MainViewModelStore.State.Idle)

    override val stateFlow: StateFlow<MainViewModelStore.State> = _stateFlow

    init {
        coroutineScope.launch {
            interaction.stateFlow.collect { response -> onResponseReceived(response) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        restoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SearchQueryKey, _query)
        outState.putString(SelectedTitleKey, selectedTitle)
        outState.putSerializable(ServiceResponseModelKey,
            response?.data?.let { ServiceResponseSummaryList(it) })

        when (_stateFlow.value) {
            MainViewModelStore.State.Idle -> {}
            is MainViewModelStore.State.OriginalSearchQuery -> {}
            is MainViewModelStore.State.Error -> {
                val error = _stateFlow.value as MainViewModelStore.State.Error
                outState.putString(ErrorMessageKey, error.throwable.message)
            }

            MainViewModelStore.State.InProgress -> outState.putBoolean(InProgressKey, true)
            is MainViewModelStore.State.Response -> {}

            is MainViewModelStore.State.ItemSelected -> {}
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        restoreInstanceState(savedInstanceState)
    }

    override fun emit(intent: MainViewModelStore.Intent) {
        coroutineScope.launch {
            when (intent) {
                is MainViewModelStore.Intent.OnSearchQueryChanged -> onOnSearchQueryChanged(intent)
                is MainViewModelStore.Intent.SubmitQueryText -> onSubmitQueryText(intent)
                MainViewModelStore.Intent.RetrieveInformation -> onRetrieveInformation()
                is MainViewModelStore.Intent.TitleSelected -> onTitleSelected(intent)
                MainViewModelStore.Intent.BackPressed -> onBackPressed()
            }
        }
    }

    private fun onBackPressed() {
        selectedTitle = ""
        onRetrieveInformation()
    }

    private fun onTitleSelected(intent: MainViewModelStore.Intent.TitleSelected) {
        selectedTitle = intent.title
        val selectedItem = this.response?.data?.firstOrNull { it.title == intent.title }
        sendResponse(MainViewModelStore.State.ItemSelected(selectedItem))
    }

    private fun onRetrieveInformation() {
        response?.let {
            sendResponse(MainViewModelStore.State.Response(ServiceResponseSummaryList(it.data)))
        } ?: run {
            sendResponse(MainViewModelStore.State.InProgress)
            emit(MainInteractionStore.Intent.Retrieve)
        }
    }

    private fun onOnSearchQueryChanged(intent: MainViewModelStore.Intent.OnSearchQueryChanged) {
        _query = intent.query ?: ""
        emit(MainInteractionStore.Intent.Search(intent.query))
    }

    private fun onSubmitQueryText(intent: MainViewModelStore.Intent.SubmitQueryText) {
        emit(MainInteractionStore.Intent.Search(intent.query))
    }

    private fun emit(intent: MainInteractionStore.Intent) {
        interaction.emit(intent)
    }

    @SuppressLint("NewApi")
    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            _query = bundle.getString(SearchQueryKey, "")
            selectedTitle = bundle.getString(SelectedTitleKey, "")

            if(bundle.containsKey(ServiceResponseModelKey)) {
                if (sdkVersion >= tiramisuVersion) {
                    (bundle.getSerializable(
                        ServiceResponseModelKey,
                        ServiceResponseSummaryList::class.java
                    ))
                } else {
                    (bundle.getSerializable(
                        ServiceResponseModelKey
                    ))
                            as ServiceResponseSummaryList?
                }?.let {
                    response = MainInteractionStore.State.Success(it.list)
                    sendResponse(MainViewModelStore.State.Response(it))
                }
            }

            if (bundle.containsKey(InProgressKey)) {
                sendResponse(MainViewModelStore.State.InProgress)
                onRetrieveInformation()
            } else if (bundle.containsKey(ErrorMessageKey)) {
                val message = bundle.getString(ErrorMessageKey, "")
                sendResponse(MainViewModelStore.State.Error(Throwable(message)))
            }

            if (_query.isNotEmpty()) {
                sendResponse(MainViewModelStore.State.OriginalSearchQuery(_query))
            }
            if (selectedTitle.isNotEmpty()) {
                emit(MainViewModelStore.Intent.TitleSelected(selectedTitle))
            }
        }
    }

    private fun onResponseReceived(response: MainInteractionStore.State) {
        when (response) {
            is MainInteractionStore.State.Error -> onError(response)
            MainInteractionStore.State.Idle -> onIdle()
            MainInteractionStore.State.InProgress -> onInProgress()
            is MainInteractionStore.State.Success -> onSuccess(response)
        }
    }

    private fun onError(response: MainInteractionStore.State.Error) {
        sendResponse(MainViewModelStore.State.Error(response.throwable))
    }

    private fun onIdle() {
        sendResponse(MainViewModelStore.State.Idle)
    }

    private fun onInProgress() {
        sendResponse(MainViewModelStore.State.InProgress)
    }

    private fun onSuccess(response: MainInteractionStore.State.Success) {
        this.response = response
        sendResponse(MainViewModelStore.State.Response(ServiceResponseSummaryList(response.data)))
    }

    private fun sendResponse(state: MainViewModelStore.State) {
        _stateFlow.tryEmit(state)
    }

    companion object {
        const val SearchQueryKey = "SearchQuery"
        const val ErrorMessageKey = "ErrorMessage"
        const val ServiceResponseModelKey = "ServiceResponseModel"
        const val InProgressKey = "InProgress"
        const val SelectedTitleKey = "SelectedTitle"
    }
}