package com.volcode.coreui.di

import com.volcode.coreui.analytics.engine.AnalyticsEngine
import com.volcode.coreui.analytics.engine.TimberAnalyticsEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UiModule {

    @Singleton
    @Provides
    fun provideAnalyticsEngine(): AnalyticsEngine = TimberAnalyticsEngine()

}