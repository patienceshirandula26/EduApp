package com.example.eduapp.model

import kotlin.math.abs
import kotlin.random.Random

/**
 * Builds the multiple-choice options for a puzzle.
 *
 * Wrong answers are generated close to the real one so a learner has to
 * actually solve the picture rather than guess by size. Pure Kotlin, so it
 * is unit tested without an emulator.
 */
object AnswerOptions {

    const val OPTION_COUNT = 4

    fun distractorsFor(answer: Int, random: Random = Random.Default): List<Int> {
        val pool = linkedSetOf<Int>()

        listOf(1, 2, 3, 4, 5, 10).shuffled(random).forEach { offset ->
            listOf(answer + offset, answer - offset).shuffled(random).forEach { candidate ->
                if (candidate >= 0 && candidate != answer) pool.add(candidate)
            }
        }

        listOf(answer * 2, answer / 2).forEach {
            if (it >= 0 && it != answer) pool.add(it)
        }

        // Guarantees enough options even when the answer is 0.
        var filler = answer + 11
        while (pool.size < OPTION_COUNT - 1) {
            if (filler != answer) pool.add(filler)
            filler++
        }

        return pool.sortedBy { abs(it - answer) }.take(OPTION_COUNT - 1)
    }

    /** The full shuffled option list for a puzzle. */
    fun optionsFor(answer: Int, random: Random = Random.Default): List<Int> =
        (distractorsFor(answer, random) + answer).shuffled(random)
}
