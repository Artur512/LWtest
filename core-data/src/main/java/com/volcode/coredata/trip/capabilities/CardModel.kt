package com.volcode.coredata.trip.capabilities

data class CardModel(
    val id: Int,
    val departure: String,
    val arrival: String,
    val seat: String? = null,
    val vehicleNumber: String? = null,
    val vehicleType: String? = null,
    val hints: ArrayList<String>? = null
)