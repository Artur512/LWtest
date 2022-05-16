package com.volcode.cards.domain.usecase

import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.service.CardService
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val service: CardService) {
    suspend fun invoke(): Result<List<Card>> {
        return try {
            val result = service.getCards()
            Result.success(result)
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }
}