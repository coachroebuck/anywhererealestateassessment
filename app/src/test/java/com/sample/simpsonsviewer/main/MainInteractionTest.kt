package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainRepositoryStore
import com.sample.simpsonsviewer.model.ServiceResponse
import com.sample.simpsonsviewer.permissions.DefaultUnitTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainInteractionTest : DefaultUnitTest() {

    private lateinit var repository: MainRepository
    private lateinit var interaction: MainInteraction
    private val stateFlow: MutableStateFlow<MainRepositoryStore.State> =
        MutableStateFlow(MainRepositoryStore.State.Idle)
    private val domain = "https://duckduckgo.com"
    private val mainRepositoryStoreIntent = slot<MainRepositoryStore.Intent>()

    @Before
    override fun setUp() {
        super.setUp()

        repository = mockk(relaxed = true)

        interaction = DefaultMainInteraction(
            repository = repository,
            coroutineScope = coroutineScope,
            domain = domain,
        )

        every { repository.stateFlow } returns stateFlow
        coEvery { repository.stateFlow } returns stateFlow
        every { repository.emit(capture(mainRepositoryStoreIntent)) } answers { }
        coEvery { repository.emit(capture(mainRepositoryStoreIntent)) } answers { }

    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun `GIVEN invoke emit WHEN intent is Search THEN verify correct state`() {
        emit(MainInteractionStore.Intent.Search(text))

        assertTrue(mainRepositoryStoreIntent.isCaptured)

        validateEquality(
            MainRepositoryStore.Intent.Search::class,
            mainRepositoryStoreIntent.captured::class
        )

        validateEquality(
            text,
            (mainRepositoryStoreIntent.captured as MainRepositoryStore.Intent.Search).query
        )
    }

    @Test
    fun `GIVEN invoke emit into stateFlow WHEN intent is Error THEN verify correct state`() {
        sendResponse(MainRepositoryStore.State.Error(Throwable(text)))
        validateEquality(
            MainInteractionStore.State.Error::class,
            interaction.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit into stateFlow WHEN intent is Idle THEN verify correct state`() {
        sendResponse(MainRepositoryStore.State.Idle)
        validateEquality(
            MainInteractionStore.State.Idle::class,
            interaction.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit into stateFlow WHEN intent is InProgress THEN verify correct state`() {
        sendResponse(MainRepositoryStore.State.InProgress)
        validateEquality(
            MainInteractionStore.State.InProgress::class,
            interaction.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit into stateFlow WHEN intent is Success THEN verify correct state`() {
        with(appSerializer) {
            val model = mockResponse.toObject<ServiceResponse>()
            sendResponse(MainRepositoryStore.State.Success(model))

            validateEquality(
                MainInteractionStore.State.Success::class,
                interaction.stateFlow.value::class
            )

            val summary = interaction.stateFlow.value as MainInteractionStore.State.Success

            validateEquality(model?.relatedTopics?.size, summary.data.size)

            for(i in 0 until summary.data.size) {
                model?.relatedTopics?.get(i)?.let { expectation ->
                    val actual = summary.data[i]
                    val index = expectation.firstURL.lastIndexOf('/')
                    val title = expectation.firstURL.substring(index).replace("_", " ")
                    val details = expectation.text
                    val url = expectation.firstURL
                    val icon = "$domain${expectation.icon.url}"

                    validateEquality(title, actual.title)
                    validateEquality(details, actual.details)
                    validateEquality(url, actual.url)
                    validateEquality(icon, actual.icon)
                }
            }
        }
    }

    private fun emit(intent: MainInteractionStore.Intent) {
        interaction.emit(intent)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    private fun sendResponse(state: MainRepositoryStore.State) {
        stateFlow.tryEmit(state)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    companion object {
        private const val text = "keywords"
    }
}