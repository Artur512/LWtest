package com.volcode.cards.domain.service

import com.volcode.cards.domain.capabilities.Stop

internal object StopsDictionary {
    val values = setOf(
        Stop("Madrid") to Stop("Barcelona"),
        Stop("Barcelona") to Stop("Girona Airport"),
        Stop("Girona Airport") to Stop("London"),
        Stop("London") to Stop("New York JFK")
    )
}