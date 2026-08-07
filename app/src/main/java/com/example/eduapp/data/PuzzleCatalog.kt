package com.example.eduapp.data

import android.content.res.AssetManager
import com.example.eduapp.model.Puzzle
import com.example.eduapp.model.PuzzleParser

/**
 * Reads every puzzle out of the assets folder at runtime.
 *
 * Adding an image to assets/ adds a quiz. No code change needed.
 */
class PuzzleCatalog(private val assets: AssetManager) {

    private var cache: List<Puzzle>? = null

    fun loadAll(): List<Puzzle> = cache ?: scan().also { cache = it }

    fun levels(): List<Int> = loadAll().map { it.level }.distinct().sorted()

    fun forLevel(level: Int): List<Puzzle> = loadAll().filter { it.level == level }

    private fun scan(): List<Puzzle> {
        val folders = assets.list("")
            ?.filter { it.toIntOrNull() != null }
            ?.sortedBy { it.toInt() }
            .orEmpty()

        return folders
            .flatMap { folder ->
                assets.list(folder).orEmpty().mapNotNull { file ->
                    PuzzleParser.parse(folder, file)
                }
            }
            .sortedWith(compareBy({ it.level }, { it.number }))
    }
}
