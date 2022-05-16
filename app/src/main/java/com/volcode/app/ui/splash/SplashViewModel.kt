package com.volcode.app.ui.splash

import androidx.lifecycle.viewModelScope
import com.viessmann.vimove.core.di.DefaultDispatcher
import com.volcode.coreui.analytics.engine.AnalyticsEngine
import com.volcode.coreui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    analyticsEngine: AnalyticsEngine
) : BaseViewModel(analyticsEngine) {

    override val screenName: String = "splash_screen"

    private val splashEventChannel = Channel<Unit>()
    val splashEventFlow = splashEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            delay(3000L)
            splashEventChannel.send(Unit)
        }
    }
}
