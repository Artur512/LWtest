package com.volcode.cards.ui.boardingcards

import com.volcode.cards.domain.capabilities.Card

sealed class BoardingViewState {
    object InProgress : BoardingViewState()
    class ShowCardsState(val cards: List<Card>, val sorted: Boolean) : BoardingViewState()
}