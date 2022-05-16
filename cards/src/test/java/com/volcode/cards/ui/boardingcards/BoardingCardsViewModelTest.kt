package com.volcode.cards.ui.boardingcards

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.usecase.GetCardsUseCase
import com.volcode.cards.domain.usecase.SortCardsUseCase
import com.volcode.cards.ui.boardingcards.BoardingViewState.ShowCardsState
import com.volcode.coretest.InstantTaskExecutorExtension
import com.volcode.coretest.getOrAwaitValue
import com.volcode.coreui.analytics.engine.AnalyticsEngine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@Extensions(
    ExtendWith(InstantTaskExecutorExtension::class),
    ExtendWith(MockKExtension::class)
)
@ExperimentalCoroutinesApi
internal class BoardingCardsViewModelTest {

    @RelaxedMockK
    private lateinit var sortCardsUseCase: SortCardsUseCase

    @RelaxedMockK
    private lateinit var getCardsUseCase: GetCardsUseCase

    @RelaxedMockK
    private lateinit var analyticsEngine: AnalyticsEngine
    private val testDispatcher = TestCoroutineDispatcher()

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private lateinit var viewModel: BoardingCardsViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when viewModel has created then init card`() {
        testDispatcher.runBlockingTest {
            val successfulResult = Result.success(listOf<Card>())
            coEvery { getCardsUseCase.invoke() } returns successfulResult

            viewModel = BoardingCardsViewModel(getCardsUseCase, sortCardsUseCase, testDispatcher, testDispatcher, analyticsEngine)

            coVerify(exactly = 1) { getCardsUseCase.invoke() }
            val viewState = viewModel.state.getOrAwaitValue() as ShowCardsState
            assertEquals(successfulResult.getOrNull(), viewState.cards)
        }
    }

    @Test
    fun `should return sorted cards`() {
        testDispatcher.runBlockingTest {
            val cards = listOf<Card>()
            val sortedCards = listOf<Card>()
            coEvery { getCardsUseCase.invoke() } returns Result.success(cards)
            coEvery { sortCardsUseCase.invoke(cards) } returns Result.success(sortedCards)
            viewModel = BoardingCardsViewModel(getCardsUseCase, sortCardsUseCase, testDispatcher, testDispatcher, analyticsEngine)

            viewModel.sortStops()

            coVerify(exactly = 1) { getCardsUseCase.invoke() }
            coVerify(exactly = 1) { sortCardsUseCase.invoke(cards) }
            val viewState = viewModel.state.getOrAwaitValue() as ShowCardsState
            assertEquals(sortedCards, viewState.cards)
        }
    }
}
