package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.api.service.SimpsonsCharactersAdapter
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import com.sample.simpsonsviewer.model.ServiceResponse
import com.sample.simpsonsviewer.serialization.AppSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call

class DefaultMainRepository(
    private val simpsonsCharactersAdapter: SimpsonsCharactersAdapter,
    private val logAdapter: LogAdapter,
    private val appSerializer: AppSerializer,
    private val coroutineScope: CoroutineScope,
) : MainRepository {

    private val _stateFlow: MutableStateFlow<MainRepositoryStore.State> =
        MutableStateFlow(MainRepositoryStore.State.Idle)

    override val stateFlow: StateFlow<MainRepositoryStore.State> = _stateFlow

    override fun emit(intent: MainRepositoryStore.Intent) {
        when (intent) {
            is MainRepositoryStore.Intent.Search -> onSearch(intent)
        }
    }

    private fun onSearch(intent: MainRepositoryStore.Intent.Search) {
        executeConnectionCall(
            call = simpsonsCharactersAdapter.start(intent.query),
            successResponse = { response ->
                sendResponse(MainRepositoryStore.State.Success(response))
            },
            failureResponse = { response ->
                sendResponse(MainRepositoryStore.State.Error(Throwable(response))
                )
            },
        )
    }

    private fun executeConnectionCall(
        call: Call<ResponseBody>?,
        successResponse: (ServiceResponse?) -> Unit,
        failureResponse: (String) -> Unit,
    ) {
        try {
            call?.execute()?.let { output ->
                if (output.isSuccessful) {
                    output.body()?.let { body ->
                        val text = body.string()
                        with(appSerializer) {
                            text.toObject<ServiceResponse>()?.let { data ->
                                successResponse.invoke(data)
                            } ?: run {
                                logAdapter.w("Response was successful, but parsed an empty object")
                                failureResponse.invoke("No Response Returned")
                            }
                        }
                    } ?: run {
                        logAdapter.w("Response was successful, but no response body received")
                        failureResponse.invoke("No response body was received")
                    }
                } else {
                    output.errorBody()?.let { body ->
                        val text = body.string()
                        logAdapter.w("Response was successful, but parsed an empty object")
                        failureResponse("API Request Failure: $text")
                    } ?: run {
                        logAdapter.w("Response was successful, but parsed an empty object")
                        failureResponse.invoke(
                            "Server error occurred. No response body was received"
                        )
                    }
                }
            } ?: run {
                logAdapter.w("Internal Client Error Occurred.")
                failureResponse.invoke("Internal Client Error Occurred.")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            logAdapter.e(t.message)
            failureResponse.invoke(t.message ?: "Internal Client Error Occurred.")
        }
    }

    private fun sendResponse(state: MainRepositoryStore.State) {
        coroutineScope.launch { _stateFlow.tryEmit(state) }
    }
}