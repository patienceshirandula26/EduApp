package com.example.eduapp.model

/** Turns a number of seconds into something readable, e.g. 95 -> "1m 35s". */
object TimeFormat {

    fun format(seconds: Int): String = when {
        seconds < 0 -> "0s"
        seconds < 60 -> "${seconds}s"
        seconds % 60 == 0 -> "${seconds / 60}m"
        else -> "${seconds / 60}m ${seconds % 60}s"
    }
}
