package com.volcode.cards.ui.boardingcards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.viessmann.vimove.core.di.DefaultDispatcher
import com.viessmann.vimove.core.di.IoDispatcher
import com.volcode.cards.domain.capabilities.Card
import com.volcode.cards.domain.usecase.GetCardsUseCase
import com.volcode.cards.domain.usecase.SortCardsUseCase
import com.volcode.cards.ui.boardingcards.BoardingViewState.InProgress
import com.volcode.cards.ui.boardingcards.BoardingViewState.ShowCardsState
import com.volcode.coreui.analytics.engine.AnalyticsEngine
import com.volcode.coreui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardingCardsViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val sortCardsUseCase: SortCardsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    analyticsEngine: AnalyticsEngine
) :
    BaseViewModel(analyticsEngine) {
    override val screenName: String = "boarding_cards"

    private val _state = MutableLiveData<BoardingViewState>()
    val state: LiveData<BoardingViewState> = _state
    private var cards: List<Card>? = null

    init {
        loadStops()
    }

    fun sortStops() {
        _state.postValue(InProgress)
        viewModelScope.launch(context = defaultDispatcher) {
            sortCardsUseCase.invoke(cards)
                .onSuccess {
                    _state.postValue(ShowCardsState(it, true))
                }
                .onFailure {
                    //TODO show message, error screen etc.
                }
        }
    }

    private fun loadStops() {
        _state.postValue(InProgress)
        viewModelScope.launch(context = ioDispatcher) {
            getCardsUseCase.invoke()
                .onSuccess { result ->
                    cards = result
                    _state.postValue(ShowCardsState(result, false))
                }
                .onFailure {
                    //TODO show message, error screen etc.
                }
        }
    }
}