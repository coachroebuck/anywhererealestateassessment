package com.sample.simpsonsviewer.main

import com.sample.simpsonsviewer.main.DefaultMainViewModel.Companion.ErrorMessageKey
import com.sample.simpsonsviewer.main.DefaultMainViewModel.Companion.InProgressKey
import com.sample.simpsonsviewer.main.DefaultMainViewModel.Companion.SearchQueryKey
import com.sample.simpsonsviewer.main.DefaultMainViewModel.Companion.ServiceResponseModelKey
import com.sample.simpsonsviewer.main.mvi.MainInteractionStore
import com.sample.simpsonsviewer.main.mvi.MainViewModelStore
import com.sample.simpsonsviewer.model.ServiceResponseSummary
import com.sample.simpsonsviewer.model.ServiceResponseSummaryList
import com.sample.simpsonsviewer.DefaultUnitTest
import com.sample.simpsonsviewer.main.DefaultMainViewModel.Companion.SelectedTitleKey
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : DefaultUnitTest() {

    private lateinit var interaction: MainInteraction
    private lateinit var viewModel: MainViewModel
    private val stateFlow: MutableStateFlow<MainInteractionStore.State> =
        MutableStateFlow(MainInteractionStore.State.Idle)
    private val mainInteractionStoreIntent = slot<MainInteractionStore.Intent>()
    private val title = "title"
    private val details = "details"
    private val icon = "icon"
    private val url = "url"
    private val list = ServiceResponseSummaryList(
        list = listOf(
            ServiceResponseSummary(
                title = title,
                details = details,
                icon = icon,
                url = url,
            )
        )
    )

    @Before
    override fun setUp() {
        super.setUp()

        interaction = mockk(relaxed = true)
        viewModel = DefaultMainViewModel(interaction, coroutineScope)

        coroutineScope.launch {
            viewModel.stateFlow.collect { state ->
                println(state)
            }
        }

        every { interaction.stateFlow } returns stateFlow
        coEvery { interaction.stateFlow } returns stateFlow
        every { interaction.emit(capture(mainInteractionStoreIntent)) } answers { }
        coEvery { interaction.emit(capture(mainInteractionStoreIntent)) } answers { }

        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    // region onCreate Unit Tests

    @Test
    fun `GIVEN invoke onCreate WHEN bundle contains SearchQueryKey THEN verify correct state`() {
        every { bundle.getString(SearchQueryKey, "") } answers { return@answers text }
        every { bundle.getString(SearchQueryKey) } answers { return@answers text }
        onCreate()

        validateEquality(
            MainViewModelStore.State.OriginalSearchQuery::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.OriginalSearchQuery

        validateEquality(text, model.query)
    }

    @Test
    fun `GIVEN invoke onCreate WHEN bundle contains ErrorMessageKey THEN verify correct state`() {
        every {
            bundle.containsKey(ErrorMessageKey)
        } answers { return@answers true }
        every { bundle.getString(ErrorMessageKey) } answers { return@answers text }
        every { bundle.getString(ErrorMessageKey, "") } answers { return@answers text }

        onCreate()

        validateEquality(
            MainViewModelStore.State.Error::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.Error

        validateEquality(text, model.throwable.message)
    }

    @Test
    fun `GIVEN invoke onCreate WHEN bundle contains ServiceResponseModelKey THEN verify correct state`() {
        every {
            bundle.containsKey(ServiceResponseModelKey)
        } answers { return@answers true }
        every { bundle.getSerializable(ServiceResponseModelKey) } answers { return@answers list }
        every { (bundle.getSerializable(
            ServiceResponseModelKey,
            ServiceResponseSummaryList::class.java
        )) } answers { return@answers list }

        onCreate()

        validateEquality(
            MainViewModelStore.State.Response::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.Response

        validateEquality(list, model.data)
    }

    @Test
    fun `GIVEN invoke onCreate WHEN bundle contains InProgressKey THEN verify correct state`() {
        every {
            bundle.containsKey(InProgressKey)
        } answers { return@answers true }

        onCreate()

        validateEquality(
            MainViewModelStore.State.InProgress::class,
            viewModel.stateFlow.value::class
        )
    }

    // endregion

    // region onRestoreInstanceState Unit Tests

    @Test
    fun `GIVEN invoke onRestoreInstanceState WHEN bundle contains SearchQueryKey THEN verify correct state`() {
        every {
            bundle.containsKey(SearchQueryKey)
        } answers { return@answers true }
        every { bundle.getString(SearchQueryKey) } answers { return@answers text }
        every { bundle.getString(SearchQueryKey, "") } answers { return@answers text }

        onRestoreInstanceState()

        validateEquality(
            MainViewModelStore.State.OriginalSearchQuery::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.OriginalSearchQuery

        validateEquality(text, model.query)
    }

    @Test
    fun `GIVEN invoke onRestoreInstanceState WHEN bundle contains ErrorMessageKey THEN verify correct state`() {
        every {
            bundle.containsKey(ErrorMessageKey)
        } answers { return@answers true }
        every { bundle.getString(ErrorMessageKey) } answers { return@answers text }
        every { bundle.getString(ErrorMessageKey, "") } answers { return@answers text }

        onRestoreInstanceState()

        validateEquality(
            MainViewModelStore.State.Error::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.Error

        validateEquality(text, model.throwable.message)
    }

    @Test
    fun `GIVEN invoke onRestoreInstanceState WHEN bundle contains ServiceResponseModelKey THEN verify correct state`() {
        every {
            bundle.containsKey(ServiceResponseModelKey)
        } answers { return@answers true }
        every { bundle.getSerializable(ServiceResponseModelKey) } answers { return@answers list }
        every { (bundle.getSerializable(
            ServiceResponseModelKey,
            ServiceResponseSummaryList::class.java
        )) } answers { return@answers list }

        onRestoreInstanceState()

        validateEquality(
            MainViewModelStore.State.Response::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.Response

        validateEquality(list, model.data)
    }

    @Test
    fun `GIVEN invoke onRestoreInstanceState WHEN bundle contains InProgressKey THEN verify correct state`() {
        every {
            bundle.containsKey(InProgressKey)
        } answers { return@answers true }

        onRestoreInstanceState()

        validateEquality(
            MainViewModelStore.State.InProgress::class,
            viewModel.stateFlow.value::class
        )
    }

    // endregion

    // region emit() Invoked

    @Test
    fun `GIVEN invoke emit WHEN intent is OnSearchQueryChanged THEN verify correct state`() {
        emit(MainViewModelStore.Intent.OnSearchQueryChanged(text))

        val querySlot = slot<String>()
        every { bundle.putString(SearchQueryKey, capture(querySlot)) } answers { }
        viewModel.onSaveInstanceState(bundle)

        assertTrue(querySlot.isCaptured)
        validateEquality(text, querySlot.captured)
    }

    @Test
    fun `GIVEN invoke emit WHEN intent is SubmitQueryText THEN verify correct state`() {
        emit(MainViewModelStore.Intent.SubmitQueryText(text))

        assertTrue(mainInteractionStoreIntent.isCaptured)

        validateEquality(
            MainInteractionStore.Intent.Search::class,
            mainInteractionStoreIntent.captured::class
        )

        val model = mainInteractionStoreIntent.captured as MainInteractionStore.Intent.Search

        validateEquality(text, model.query)
    }

    // endregion

    // region Responses from Interaction

    @Test
    fun `GIVEN invoke emit from interaction WHEN state is Error THEN verify correct state`() {
        sendResponse(MainInteractionStore.State.Error(Throwable(text)))
    }

    @Test
    fun `GIVEN invoke emit from interaction WHEN state is Idle THEN verify correct state`() {
        sendResponse(MainInteractionStore.State.Idle)

        validateEquality(
            MainViewModelStore.State.Idle::class,
            viewModel.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit from interaction WHEN state is InProgress THEN verify correct state`() {
        sendResponse(MainInteractionStore.State.InProgress)

        validateEquality(
            MainViewModelStore.State.InProgress::class,
            viewModel.stateFlow.value::class
        )
    }

    @Test
    fun `GIVEN invoke emit from interaction WHEN state is Success THEN verify correct state`() {
        sendResponse(MainInteractionStore.State.Success(list.list))

        validateEquality(
            MainViewModelStore.State.Response::class,
            viewModel.stateFlow.value::class
        )

        val model = viewModel.stateFlow.value as MainViewModelStore.State.Response

        validateEquality(list, model.data)
    }

    // endregion

    // region Private Methods

    private fun onCreate() {
        viewModel.onCreate(bundle)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    private fun onRestoreInstanceState() {
        viewModel.onRestoreInstanceState(bundle)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    private fun emit(intent: MainViewModelStore.Intent) {
        viewModel.emit(intent)
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    private fun sendResponse(state: MainInteractionStore.State) {
        stateFlow.value = state
        runBlocking { coroutineScope.testScheduler.advanceUntilIdle() }
    }

    // endregion
}