package com.volcode.cards.domain.usecase

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.service.CardService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@Extensions(
    ExtendWith(MockKExtension::class)
)
internal class SortCardsUseCaseTest {

    @RelaxedMockK
    private lateinit var service: CardService

    lateinit var sut: SortCardsUseCase

    @BeforeEach
    fun setup() {
        sut = SortCardsUseCase(service)
    }

    @Test
    fun `should sort list of cards successfully`() {
        val cards = listOf(mockk<Card>())
        val sortedCards = listOf(mockk<Card>())

        coEvery { service.sortStops(cards) } returns sortedCards

        assertEquals(Result.success(sortedCards), sut.invoke(cards))
        coVerify { sut.invoke(cards) }
    }


    @Test
    fun `should return failure when list of cards is null`() {
        val result = sut.invoke(null)

        assertTrue(result.isFailure)
    }

}