package com.example.eduapp

import androidx.test.platform.app.InstrumentationRegistry
import com.example.eduapp.data.PuzzleCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Instrumented because it reads the real assets folder, which only exists
 * on a device.
 */
class PuzzleCatalogTest {

    private val catalog = PuzzleCatalog(
        InstrumentationRegistry.getInstrumentation().targetContext.assets
    )

    @Test
    fun everyPuzzleInAssetsIsFound() {
        assertEquals(18, catalog.loadAll().size)
    }

    @Test
    fun thereAreThreeLevels() {
        assertEquals(listOf(1, 2, 3), catalog.levels())
    }

    @Test
    fun eachLevelHasSixPuzzles() {
        listOf(1, 2, 3).forEach { level ->
            assertEquals(6, catalog.forLevel(level).size)
        }
    }

    @Test
    fun answersAreReadFromTheFilenames() {
        val first = catalog.forLevel(1).first { it.assetPath.contains("pic01") }
        assertEquals(0, first.answer)
    }

    @Test
    fun noAnswerIsNegative() {
        assertTrue(catalog.loadAll().all { it.answer >= 0 })
    }
}
