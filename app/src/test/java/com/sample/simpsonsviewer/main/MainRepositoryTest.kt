package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.api.service.SimpsonsCharactersAdapter
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import com.sample.simpsonsviewer.permissions.DefaultUnitTest
import com.sample.simpsonsviewer.serialization.AppSerializer
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryTest : DefaultUnitTest() {

    private lateinit var responseBody: ResponseBody
    private lateinit var errorBody: ResponseBody
    private lateinit var response: Response<ResponseBody>
    private lateinit var remoteCall: Call<ResponseBody>
    private lateinit var simpsonsCharactersAdapter: SimpsonsCharactersAdapter
    private lateinit var repository: MainRepository
    private var code = 200
    private var isSuccessful = true
    private val headers = Headers.headersOf(
        "number",
        abs(Random.nextInt()).toString(),
    )
    
    @Before
    override fun setUp() {
        super.setUp()
        remoteCall = mockk(relaxed = true)
        response = mockk(relaxed = true)
        simpsonsCharactersAdapter = mockk(relaxed = true)
        repository = DefaultMainRepository(
            simpsonsCharactersAdapter = simpsonsCharactersAdapter,
                    logAdapter = logAdapter,
                    appSerializer = appSerializer,
            coroutineScope = coroutineScope,
        )

        val successJsonObject = JSONObject()
        successJsonObject.put("status", "active")
        val errorJsonObject = JSONObject()
        errorJsonObject.put("message", "Unit test error message")

        responseBody = mockResponse.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
        errorBody = errorJsonObject.toString()
            .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())

        every { simpsonsCharactersAdapter.start(text) } answers {
            return@answers remoteCall
        }
        every { remoteCall.execute() } answers {
            return@answers response
        }
        every { response.isSuccessful } answers {
            return@answers isSuccessful
        }
        every { response.code() } answers {
            return@answers code
        }
        every { response.headers() } answers {
            return@answers headers
        }
        every { response.body() } answers {
            return@answers responseBody
        }
        every { response.errorBody() } answers {
            return@answers errorBody
        }
    }

    @Test
    fun `GIVEN invoke emit WHEN intent is Search and operation is successful THEN verify correct state`() {
        emit(MainRepositoryStore.Intent.Search(text))

        validateEquality(
            MainRepositoryStore.State.Success::class,
            repository.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit WHEN intent is Search but response is not successful THEN verify correct state`() {
        isSuccessful = false
        code = 400

        emit(MainRepositoryStore.Intent.Search(text))

        validateEquality(
            MainRepositoryStore.State.Error::class,
            repository.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit WHEN intent is Search but response body is empty THEN verify correct state`() {

        val successJsonObject = JSONObject()

        responseBody = successJsonObject.toString()
            .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())

        emit(MainRepositoryStore.Intent.Search(text))

        validateEquality(
            MainRepositoryStore.State.Error::class,
            repository.stateFlow.value::class
        )
    }

    @Test(expected = IllegalStateException::class)
    fun `GIVEN invoke emit WHEN intent is Search but service fails THEN verify correct state`() {

        every { simpsonsCharactersAdapter.start(text) } throws IllegalStateException("Unit test error")

        emit(MainRepositoryStore.Intent.Search(text))

        validateEquality(
            MainRepositoryStore.State.Error::class,
            repository.stateFlow.value::class
        )
    }

    private fun emit(intent: MainRepositoryStore.Intent) {
        repository.emit(intent)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    companion object {
        private const val text = "keywords"
    }
}