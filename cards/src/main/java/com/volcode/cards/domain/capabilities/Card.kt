package com.volcode.cards.domain.capabilities

import com.volcode.coredata.trip.capabilities.CardModel

data class Card(
    val id: Int,
    val departure: Stop,
    val arrival: Stop,
    val seat: String? = null,
    val vehicleNumber: String? = null,
    val vehicleType: String? = null,
    val hints: List<String>? = null
) {
    companion object {
        fun from(cardModel: CardModel): Card =
            Card(
                cardModel.id,
                Stop(cardModel.departure),
                Stop(cardModel.arrival),
                cardModel.seat,
                cardModel.vehicleNumber,
                cardModel.vehicleType,
                cardModel.hints
            )
    }
}