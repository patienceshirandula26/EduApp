package com.example.eduapp.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatTest {

    @Test
    fun `seconds under a minute show as seconds`() {
        assertEquals("0s", TimeFormat.format(0))
        assertEquals("45s", TimeFormat.format(45))
        assertEquals("59s", TimeFormat.format(59))
    }

    @Test
    fun `whole minutes drop the seconds`() {
        assertEquals("1m", TimeFormat.format(60))
        assertEquals("3m", TimeFormat.format(180))
    }

    @Test
    fun `mixed times show both parts`() {
        assertEquals("1m 35s", TimeFormat.format(95))
        assertEquals("2m 1s", TimeFormat.format(121))
    }

    @Test
    fun `negative input is treated as zero`() {
        assertEquals("0s", TimeFormat.format(-5))
    }
}
