package com.volcode.coredata.trip.repository

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.volcode.coredata.R
import com.volcode.coredata.jsonfilereader.JsonFileReader
import com.volcode.coredata.trip.capabilities.CardModel
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import com.volcode.coredata.trip.capabilities.errors.UnableLoadFileException
import dagger.hilt.android.scopes.ServiceScoped
import java.lang.reflect.Type


@ServiceScoped
internal class CardsRepositoryImpl(private val jsonFileReader: JsonFileReader, private val gson: Gson) : CardsRepository {

    @Throws(ParseToModelException::class, UnableLoadFileException::class)
    override suspend fun loadCards(): List<CardModel> {
        return try {
            val text = jsonFileReader.readFile(R.raw.cards)
            val userListType: Type = object : TypeToken<List<CardModel?>?>() {}.type
            gson.fromJson(text, userListType)
        } catch (exc: Resources.NotFoundException) {
            throw UnableLoadFileException()
        } catch (exc: JsonParseException) {
            throw ParseToModelException()
        } catch (exc: JsonSyntaxException) {
            throw ParseToModelException()
        }
    }
}