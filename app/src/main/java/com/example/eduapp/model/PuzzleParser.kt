package com.example.eduapp.model

/**
 * Turns an asset filename into a Puzzle.
 *
 * Deliberately contains no Android code so it can be unit tested on the JVM
 * without an emulator.
 */
object PuzzleParser {

    private val PATTERN = Regex(
        "^level(\\d+)_pic(\\d+)_(\\d+)\\.(png|jpe?g|webp)$",
        RegexOption.IGNORE_CASE
    )

    /** Returns null for any file that doesn't follow the naming convention. */
    fun parse(folder: String, fileName: String): Puzzle? {
        val match = PATTERN.find(fileName) ?: return null
        val (level, number, answer) = match.destructured
        return Puzzle(
            id = fileName.substringBeforeLast('.'),
            level = level.toInt(),
            number = number.toInt(),
            assetPath = "$folder/$fileName",
            answer = answer.toInt()
        )
    }
}
