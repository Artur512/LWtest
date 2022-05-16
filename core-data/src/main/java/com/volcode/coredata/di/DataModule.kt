package com.volcode.coredata.di

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.volcode.coredata.jsonfilereader.JsonFileReader
import com.volcode.coredata.trip.repository.CardsRepository
import com.volcode.coredata.trip.repository.CardsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideTripRepository(jsonFileReader: JsonFileReader, gson: Gson): CardsRepository =
        CardsRepositoryImpl(jsonFileReader, gson)


    @Singleton
    @Provides
    fun provideRepository(resources: Resources) = JsonFileReader(resources)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}