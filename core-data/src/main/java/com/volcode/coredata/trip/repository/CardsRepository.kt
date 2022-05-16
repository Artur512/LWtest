package com.volcode.coredata.trip.repository

import com.volcode.coredata.trip.capabilities.CardModel
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import com.volcode.coredata.trip.capabilities.errors.UnableLoadFileException


interface CardsRepository {
    @Throws(ParseToModelException::class, UnableLoadFileException::class)
    suspend fun loadCards(): List<CardModel>
}