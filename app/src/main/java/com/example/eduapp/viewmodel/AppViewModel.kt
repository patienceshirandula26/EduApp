package com.example.eduapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.data.ResultRepository
import com.example.eduapp.database.QuizResult
import com.example.eduapp.model.Puzzle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(
    private val puzzleRepository: PuzzleRepository,
    private val resultRepository: ResultRepository
) : ViewModel() {

    fun availableLevels(): List<Int> = puzzleRepository.availableLevels()

    fun puzzlesForLevel(level: Int): List<Puzzle> = puzzleRepository.puzzlesForLevel(level)

    fun totalPuzzleCount(): Int = puzzleRepository.allPuzzles().size

    fun resultsFor(username: String): Flow<List<QuizResult>> =
        resultRepository.resultsFor(username)

    fun saveResult(result: QuizResult) {
        viewModelScope.launch { resultRepository.saveResult(result) }
    }

    fun clearResults(username: String) {
        viewModelScope.launch { resultRepository.clearFor(username) }
    }
}
