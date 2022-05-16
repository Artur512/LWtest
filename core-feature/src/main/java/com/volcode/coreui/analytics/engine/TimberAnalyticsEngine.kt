package com.volcode.coreui.analytics.engine

import timber.log.Timber

class TimberAnalyticsEngine : AnalyticsEngine {

    override fun logEvent(event: String, parameter: String) {
        Timber.i("logEvent event: $event, parameter: $parameter")
    }
}
