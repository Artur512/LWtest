package com.volcode.cards.domain.capabilities

data class Stop(val name: String) {
    init {
        assert(name.isNotEmpty()) { throw IllegalStateException(" Stop name is empty") }
        assert(name.isNotBlank()) { throw IllegalStateException(" Stop name is blank") }
    }
}