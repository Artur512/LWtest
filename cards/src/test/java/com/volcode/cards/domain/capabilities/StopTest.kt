package com.volcode.cards.domain.capabilities

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class StopTest {

    @Test
    fun `when stop name is blank throw exception`() {
        assertThrows<IllegalStateException> {
            Stop(" ")
        }
    }

    @Test
    fun `when stop name is empty throw exception`() {
        assertThrows<IllegalStateException> {
            Stop("")
        }
    }

}