package com.volcode.coreui.base

import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.volcode.coreui.analytics.Event
import com.volcode.coreui.analytics.engine.AnalyticsEngine

abstract class BaseViewModel(private val analyticsEngine: AnalyticsEngine) : ViewModel(), DefaultLifecycleObserver {
    abstract val screenName: String

    @CallSuper
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        analyticsEngine.logEvent(Event.SCREEN_NAME, screenName)
    }
}