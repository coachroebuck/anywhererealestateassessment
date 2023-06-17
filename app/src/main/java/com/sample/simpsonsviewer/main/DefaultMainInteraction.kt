package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import com.sample.simpsonsviewer.model.ServiceResponse
import com.sample.simpsonsviewer.model.ServiceResponseSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMainInteraction(
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope,
    private val domain: String,
) : MainInteraction {
    private val _stateFlow: MutableStateFlow<MainInteractionStore.State> =
        MutableStateFlow(MainInteractionStore.State.Idle)

    override val stateFlow: StateFlow<MainInteractionStore.State> = _stateFlow

    private var response: ServiceResponse? = null

    init {
        coroutineScope.launch {
            repository.stateFlow.collect { response -> onResponseReceived(response) }
        }
    }

    override fun emit(intent: MainInteractionStore.Intent) {
        when (intent) {
            is MainInteractionStore.Intent.Search -> onSearch(intent)
        }
    }

    private fun onSearch(intent: MainInteractionStore.Intent.Search) {
        repository.emit(MainRepositoryStore.Intent.Search(intent.query))
    }

    private fun onResponseReceived(response: MainRepositoryStore.State) {
        when (response) {
            is MainRepositoryStore.State.Error -> onError(response)
            MainRepositoryStore.State.Idle -> onIdle()
            MainRepositoryStore.State.InProgress -> onInProgress()
            is MainRepositoryStore.State.Success -> onSuccess(response)
        }
    }

    private fun onError(response: MainRepositoryStore.State.Error) {
        sendResponse(MainInteractionStore.State.Error(response.throwable))
    }

    private fun onIdle() {
        sendResponse(MainInteractionStore.State.Idle)
    }

    private fun onInProgress() {
        sendResponse(MainInteractionStore.State.InProgress)
    }

    private fun onSuccess(response: MainRepositoryStore.State.Success) {
        val mutableList = mutableListOf<ServiceResponseSummary>()
        this.response = response.data

        response.data?.relatedTopics?.map {
            /*
            Example Model:
            {
"FirstURL": "https://duckduckgo.com/Bender_(Futurama)",
"Icon": {
"Height": "",
"URL": "/i/cb4121fd.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Bender_(Futurama)\">Bender (Futurama)</a><br>Bender Bending Rodríguez is one of the main characters in the animated television series Futurama. He was conceived by the series' creators Matt Groening and David X. Cohen, and is voiced by John DiMaggio.",
"Text": "Bender (Futurama) - Bender Bending Rodríguez is one of the main characters in the animated television series Futurama. He was conceived by the series' creators Matt Groening and David X. Cohen, and is voiced by John DiMaggio."
},

URL of /i/cb4121fd.png" => https://duckduckgo.com/i/cb4121fd.png
             */
            val index = it.firstURL.lastIndexOf('/')
            val title = it.firstURL.substring(index).replace("_", " ")
            val details = it.text
            val url = it.firstURL
            val icon = "$domain${it.icon.url}"

            mutableList.add(
                ServiceResponseSummary(
                    title = title,
                    details = details,
                    url = url,
                    icon = icon,
                )
            )
        }
        sendResponse(MainInteractionStore.State.Success(mutableList.toList()))
    }

    private fun sendResponse(state: MainInteractionStore.State) {
        coroutineScope.launch { _stateFlow.emit(state) }
    }

}