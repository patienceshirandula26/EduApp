package com.example.eduapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.data.ResultRepository
import com.example.eduapp.database.User
import com.example.eduapp.model.Puzzle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(
    private val puzzleRepository: PuzzleRepository,
    private val resultRepository: ResultRepository
) : ViewModel() {

    val results: Flow<List<User>> = resultRepository.results

    fun availableLevels(): List<Int> = puzzleRepository.availableLevels()

    fun puzzlesForLevel(level: Int): List<Puzzle> = puzzleRepository.puzzlesForLevel(level)

    fun totalPuzzleCount(): Int = puzzleRepository.allPuzzles().size

    fun addUser(username: String) {
        viewModelScope.launch {
            resultRepository.saveResult(User(username = username))
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            resultRepository.clearResults()
        }
    }
}
