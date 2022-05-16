package com.volcode.coreui.analytics.engine

interface AnalyticsEngine {
    fun logEvent(event: String, parameter: String)
}