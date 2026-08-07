package com.example.eduapp.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PuzzleParserTest {

    @Test
    fun `parses a png puzzle filename`() {
        val puzzle = PuzzleParser.parse("1", "level01_pic01_0.png")

        assertEquals(1, puzzle?.level)
        assertEquals(1, puzzle?.number)
        assertEquals(0, puzzle?.answer)
    }

    @Test
    fun `parses a jpg puzzle filename`() {
        val puzzle = PuzzleParser.parse("2", "level02_pic06_63.jpg")

        assertEquals(2, puzzle?.level)
        assertEquals(6, puzzle?.number)
        assertEquals(63, puzzle?.answer)
    }

    @Test
    fun `builds an asset path that includes the folder`() {
        val puzzle = PuzzleParser.parse("3", "level03_pic04_24.jpg")
        assertEquals("3/level03_pic04_24.jpg", puzzle?.assetPath)
    }

    @Test
    fun `id drops the file extension`() {
        val puzzle = PuzzleParser.parse("1", "level01_pic03_15.png")
        assertEquals("level01_pic03_15", puzzle?.id)
    }

    @Test
    fun `an answer of zero is parsed, not treated as missing`() {
        assertEquals(0, PuzzleParser.parse("1", "level01_pic06_0.png")?.answer)
    }

    @Test
    fun `two digit answers are parsed in full`() {
        assertEquals(55, PuzzleParser.parse("1", "level01_pic04_55.jpg")?.answer)
    }

    @Test
    fun `files that do not follow the convention are ignored`() {
        assertNull(PuzzleParser.parse("1", "notes.txt"))
        assertNull(PuzzleParser.parse("1", "level01_pic01.png"))
        assertNull(PuzzleParser.parse("1", "random_image.png"))
    }
}
