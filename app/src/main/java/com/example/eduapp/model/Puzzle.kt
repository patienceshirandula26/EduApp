package com.example.eduapp.model

/**
 * One picture quiz.
 *
 * The answer is never hardcoded. It is read from the asset filename, which
 * follows level{LL}_pic{NN}_{ANSWER}.{ext}
 * e.g. "level01_pic01_0.png" is level 1, puzzle 1, answer 0.
 */
data class Puzzle(
    val id: String,
    val level: Int,
    val number: Int,
    val assetPath: String,
    val answer: Int
)
