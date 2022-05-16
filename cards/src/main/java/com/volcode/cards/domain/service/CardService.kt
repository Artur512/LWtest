package com.volcode.cards.domain.service

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.capabilities.exceptions.UnableToLoadCardsException
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import com.volcode.coredata.trip.capabilities.errors.UnableLoadFileException
import com.volcode.coredata.trip.repository.CardsRepository
import javax.inject.Inject

class CardService @Inject constructor(private val cardsRepository: CardsRepository) {

    @Throws(UnableToLoadCardsException::class)
    suspend fun getCards(): List<Card> {
        try {
            return cardsRepository.loadCards().map { Card.from(it) }
        } catch (exc: ParseToModelException) {
            throw UnableToLoadCardsException("file format exception")
        } catch (exc: UnableLoadFileException) {
            throw UnableToLoadCardsException("file doesn't exist")
        }
    }

    fun sortStops(cards: List<Card>): List<Card> {
        return sort(cards)
    }

    private fun sort(items: List<Card>): List<Card> {
        if (items.count() < 2) {
            return items
        }
        val pivot = items[items.count() / 2]

        val equal = items.filter { it == pivot }
        val indexOfPivot = StopsDictionary.values.indexOf(Pair(pivot.departure, pivot.arrival))
        val less = items.filter {
            val indexOfElement = StopsDictionary.values.indexOf(Pair(it.departure, it.arrival))
            indexOfElement < indexOfPivot
        }
        val greater = items.filter {
            val indexOfElement = StopsDictionary.values.indexOf(Pair(it.departure, it.arrival))
            indexOfElement > indexOfPivot
        }
        return sort(less) + equal + sort(greater)
    }
}