package com.example.eduapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.data.AppSettings
import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.data.ResultRepository
import com.example.eduapp.data.UserPreferences
import com.example.eduapp.data.WordRepository
import com.example.eduapp.data.WordState
import com.example.eduapp.database.QuizResult
import com.example.eduapp.helper.SoundPlayer
import com.example.eduapp.model.Puzzle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val puzzleRepository: PuzzleRepository,
    private val resultRepository: ResultRepository,
    private val userPreferences: UserPreferences,
    private val soundPlayer: SoundPlayer,
    private val wordRepository: WordRepository
) : ViewModel() {

    private val _wordOfDay = MutableStateFlow<WordState>(WordState.Loading)
    val wordOfDay: StateFlow<WordState> = _wordOfDay.asStateFlow()

    val settings: StateFlow<AppSettings> = userPreferences.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())

    init {
        loadWordOfDay()
    }

    fun loadWordOfDay() {
        viewModelScope.launch {
            _wordOfDay.value = WordState.Loading
            _wordOfDay.value = wordRepository.wordOfTheDay()
        }
    }

    fun setUsername(name: String) {
        viewModelScope.launch { userPreferences.setUsername(name) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { userPreferences.setSoundEnabled(enabled) }
    }

    fun setVolume(value: Float) {
        viewModelScope.launch { userPreferences.setVolume(value) }
    }

    fun setCountdownEnabled(enabled: Boolean) {
        viewModelScope.launch { userPreferences.setCountdownEnabled(enabled) }
    }

    fun signOut() {
        viewModelScope.launch { userPreferences.signOut() }
    }

    fun playCorrectSound() {
        val s = settings.value
        soundPlayer.playCorrect(s.soundEnabled, s.volume)
    }

    fun playWrongSound() {
        val s = settings.value
        soundPlayer.playWrong(s.soundEnabled, s.volume)
    }

    fun playRoundFinishedSound(isPerfect: Boolean) {
        val s = settings.value
        if (isPerfect) soundPlayer.playPerfectScore(s.soundEnabled, s.volume)
        else soundPlayer.playLevelComplete(s.soundEnabled, s.volume)
    }

    fun availableLevels(): List<Int> = puzzleRepository.availableLevels()

    fun puzzlesForLevel(level: Int): List<Puzzle> = puzzleRepository.puzzlesForLevel(level)

    fun totalPuzzleCount(): Int = puzzleRepository.allPuzzles().size

    fun puzzleCountForLevel(level: Int): Int = puzzleRepository.puzzlesForLevel(level).size

    fun randomLevel(): Int = puzzleRepository.availableLevels().random()

    fun bestForLevel(username: String, level: Int): Flow<Int> =
        resultRepository.bestForLevel(username, level)

    fun resultsFor(username: String): Flow<List<QuizResult>> =
        resultRepository.resultsFor(username)

    fun quizzesPlayed(username: String): Flow<Int> = resultRepository.quizzesPlayed(username)

    fun totalCorrect(username: String): Flow<Int> = resultRepository.totalCorrect(username)

    fun totalAttempted(username: String): Flow<Int> = resultRepository.totalAttempted(username)

    fun bestScore(username: String): Flow<Int> = resultRepository.bestScore(username)

    fun averageDuration(username: String): Flow<Double> = resultRepository.averageDuration(username)

    fun fastestRound(username: String): Flow<Int> = resultRepository.fastestRound(username)

    fun saveQuizResult(level: Int, correct: Int, total: Int, durationSeconds: Int) {
        viewModelScope.launch {
            // Read from DataStore directly - settings.value can be stale when
            // no screen is currently collecting it.
            val name = userPreferences.settings.first().username
            if (name.isBlank()) return@launch
            resultRepository.saveResult(
                QuizResult(
                    username = name,
                    level = level,
                    correct = correct,
                    total = total,
                    durationSeconds = durationSeconds
                )
            )
        }
    }

    fun saveResult(result: QuizResult) {
        viewModelScope.launch { resultRepository.saveResult(result) }
    }

    fun clearResults(username: String) {
        viewModelScope.launch { resultRepository.clearFor(username) }
    }

    fun clearMyResults() {
        viewModelScope.launch {
            val name = userPreferences.settings.first().username
            if (name.isNotBlank()) resultRepository.clearFor(name)
        }
    }
}
