package com.example.eduapp.data

import com.example.eduapp.model.Puzzle

/**
 * Repository pattern: the ViewModel asks for puzzles without knowing or
 * caring that they come from the assets folder.
 */
interface PuzzleRepository {
    fun allPuzzles(): List<Puzzle>
    fun puzzlesForLevel(level: Int): List<Puzzle>
    fun availableLevels(): List<Int>
}

class PuzzleRepositoryImpl(private val catalog: PuzzleCatalog) : PuzzleRepository {

    override fun allPuzzles(): List<Puzzle> = catalog.loadAll()

    override fun puzzlesForLevel(level: Int): List<Puzzle> = catalog.forLevel(level)

    override fun availableLevels(): List<Int> = catalog.levels()
}