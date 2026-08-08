package com.example.eduapp.data

import com.example.eduapp.network.WordApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar

/** Loading / Success / Error, so the UI never sees a raw exception. */
sealed interface WordState {
    data object Loading : WordState
    data class Success(val word: String, val definition: String) : WordState
    data class Error(val message: String) : WordState
}

interface WordRepository {
    suspend fun wordOfTheDay(): WordState
}

class WordRepositoryImpl(private val api: WordApi) : WordRepository {

    // Rotates by day of year, so everyone sees the same word on the same day.
    private val words = listOf(
        "puzzle", "logic", "pattern", "equation", "number",
        "solve", "clever", "sequence", "measure", "total"
    )

    override suspend fun wordOfTheDay(): WordState = withContext(Dispatchers.IO) {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val word = words[day % words.size]
        try {
            val definition = api.lookup(word)
                .firstOrNull()?.meanings?.firstOrNull()
                ?.definitions?.firstOrNull()?.definition

            if (definition.isNullOrBlank()) WordState.Error("No definition found today.")
            else WordState.Success(word, definition)
        } catch (e: IOException) {
            WordState.Error("You're offline. Connect to see today's word.")
        } catch (e: Exception) {
            WordState.Error("Couldn't load today's word.")
        }
    }
}
