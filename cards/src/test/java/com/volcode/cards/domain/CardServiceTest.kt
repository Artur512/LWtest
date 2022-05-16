package com.volcode.cards.domain

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.capabilities.Stop
import com.volcode.cards.domain.capabilities.exceptions.UnableToLoadCardsException
import com.volcode.cards.domain.service.CardService
import com.volcode.coredata.trip.capabilities.CardModel
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import com.volcode.coredata.trip.capabilities.errors.UnableLoadFileException
import com.volcode.coredata.trip.repository.CardsRepository
import com.volcode.coretest.InstantTaskExecutorExtension
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
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@Extensions(
    ExtendWith(InstantTaskExecutorExtension::class),
    ExtendWith(MockKExtension::class)
)
@ExperimentalCoroutinesApi
internal class CardServiceTest {

    @RelaxedMockK
    private lateinit var cardsRepository: CardsRepository
    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var sut: CardService

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = CardService(cardsRepository)
    }

    @Test
    fun `should fetch cards from repository successfully`() {
        testDispatcher.runBlockingTest {
            val cardModel = CardModel(1, "Madrid", "Barcelona")
            val card = listOf(Card.from(cardModel))
            coEvery { cardsRepository.loadCards() } returns listOf(cardModel)

            val result = sut.getCards()

            assertEquals(result, card)
            coVerify(exactly = 1) { cardsRepository.loadCards() }
        }
    }

    @Test
    fun `should throw exception when file with cards hasn't loaded`() {
        testDispatcher.runBlockingTest {
            coEvery { cardsRepository.loadCards() } throws UnableLoadFileException()

            assertThrows<UnableToLoadCardsException>("file doesn't exist") {
                sut.getCards()
            }
        }
    }

    @Test
    fun `should throw exception when file is not valid json`() {
        testDispatcher.runBlockingTest {
            coEvery { cardsRepository.loadCards() } throws ParseToModelException()

            assertThrows<UnableToLoadCardsException>("file format exception") {
                sut.getCards()
            }
        }
    }

    @Test
    fun `should return sorted cards`() {
        val notSortedCards = listOf(
            Card(1, departure = Stop("Girona Airport"), arrival = Stop("London")),
            Card(4, departure = Stop("Madrid"), arrival = Stop("Barcelona")),
            Card(2, departure = Stop("Barcelona"), arrival = Stop("Girona Airport")),
            Card(3, departure = Stop("London"), arrival = Stop("New York JFK")),
        )

        val result = sut.sortStops(notSortedCards)

        val expectedSortedCards = listOf(
            Card(4, departure = Stop("Madrid"), arrival = Stop("Barcelona")),
            Card(2, departure = Stop("Barcelona"), arrival = Stop("Girona Airport")),
            Card(1, departure = Stop("Girona Airport"), arrival = Stop("London")),
            Card(3, departure = Stop("London"), arrival = Stop("New York JFK"))
        )
        assertEquals(expectedSortedCards, result)
    }
}