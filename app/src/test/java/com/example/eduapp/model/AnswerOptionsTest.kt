package com.example.eduapp.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class AnswerOptionsTest {

    @Test
    fun `always produces four options`() {
        listOf(0, 4, 15, 55, 63).forEach { answer ->
            assertEquals(4, AnswerOptions.optionsFor(answer, Random(1)).size)
        }
    }

    @Test
    fun `the correct answer is always included`() {
        listOf(0, 4, 15, 55, 63).forEach { answer ->
            assertTrue(AnswerOptions.optionsFor(answer, Random(2)).contains(answer))
        }
    }

    @Test
    fun `options are never duplicated`() {
        val options = AnswerOptions.optionsFor(21, Random(3))
        assertEquals(options.size, options.distinct().size)
    }

    @Test
    fun `no negative options are offered`() {
        val options = AnswerOptions.optionsFor(0, Random(4))
        assertTrue(options.all { it >= 0 })
    }

    @Test
    fun `an answer of zero still gets three distractors`() {
        val distractors = AnswerOptions.distractorsFor(0, Random(5))
        assertEquals(3, distractors.size)
        assertFalse(distractors.contains(0))
    }

    @Test
    fun `distractors stay close to the answer`() {
        AnswerOptions.distractorsFor(26, Random(6)).forEach {
            assertTrue("$it is too far from 26", kotlin.math.abs(it - 26) <= 26)
        }
    }
}
