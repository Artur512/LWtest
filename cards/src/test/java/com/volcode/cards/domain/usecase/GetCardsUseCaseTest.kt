package com.volcode.cards.domain.usecase

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.service.CardService
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions


@ExperimentalCoroutinesApi
@Extensions(
    ExtendWith(MockKExtension::class)
)
internal class GetCardsUseCaseTest {

    @RelaxedMockK
    private lateinit var service: CardService

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var sut: GetCardsUseCase

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @BeforeEach
    fun setup() {
        sut = GetCardsUseCase(service)
    }

    @Test
    fun `should return list of cards successfully`() {
        val cards = listOf<Card>(mockk())
        coEvery { service.getCards() } returns cards

        testDispatcher.runBlockingTest {
            assertEquals(Result.success(cards), sut.invoke())
        }
    }

    @Test
    fun `should return failure when service throw error`() {
        testDispatcher.runBlockingTest {
            val expectedException = ParseToModelException()
            coEvery { service.getCards() } throws expectedException

            val result = sut.invoke()

            assertTrue(result.isFailure)
            assertEquals(result.exceptionOrNull(), expectedException)
        }
    }
}