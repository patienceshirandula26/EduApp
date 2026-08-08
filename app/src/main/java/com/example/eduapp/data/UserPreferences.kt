package com.example.eduapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "picquiz_prefs")

/** Everything the player has chosen, held in one object. */
data class AppSettings(
    val username: String = "",
    val soundEnabled: Boolean = true,
    val volume: Float = 0.7f,
    val countdownEnabled: Boolean = false
) {
    val hasUsername: Boolean get() = username.isNotBlank()
}

/**
 * DataStore replaces SharedPreferences: it's asynchronous and exposes a Flow,
 * so a preference change repaints the UI immediately.
 */
class UserPreferences(private val context: Context) {

    private object Keys {
        val USERNAME = stringPreferencesKey("username")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VOLUME = floatPreferencesKey("volume")
        val COUNTDOWN = booleanPreferencesKey("countdown_enabled")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            username = prefs[Keys.USERNAME].orEmpty(),
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            volume = prefs[Keys.VOLUME] ?: 0.7f,
            countdownEnabled = prefs[Keys.COUNTDOWN] ?: false
        )
    }

    suspend fun setUsername(value: String) {
        context.dataStore.edit { it[Keys.USERNAME] = value.trim() }
    }

    suspend fun setSoundEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = value }
    }

    suspend fun setVolume(value: Float) {
        context.dataStore.edit { it[Keys.VOLUME] = value.coerceIn(0f, 1f) }
    }

    suspend fun setCountdownEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.COUNTDOWN] = value }
    }

    suspend fun signOut() {
        context.dataStore.edit { it.remove(Keys.USERNAME) }
    }
}
