package com.volcode.cards.domain.usecase

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.service.CardService
import javax.inject.Inject

class SortCardsUseCase @Inject constructor(private val service: CardService) {
    fun invoke(cards: List<Card>?): Result<List<Card>> {
        return try {
            requireNotNull(cards)
            val result = service.sortStops(cards)
            Result.success(result)
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }
}