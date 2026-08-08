package com.example.eduapp.ui.theme

import androidx.compose.ui.graphics.Color

// Warm and playful. The puzzle art is pastel, so the app around it goes rich
// and saturated to make the pictures pop.

val Coral = Color(0xFFFF6B6B)
val CoralDeep = Color(0xFFE04B6E)
val Magenta = Color(0xFFC44FA0)
val Plum = Color(0xFF2D1B3D)
val PlumLight = Color(0xFF4A2E5C)

val Sunshine = Color(0xFFFFB84D)
val SunshineSoft = Color(0xFFFFE0B2)

val Mint = Color(0xFF2ED9A0)
val MintSoft = Color(0xFFB8F5E0)

val CreamBg = Color(0xFFFFF6F0)
val CreamCard = Color(0xFFFFFFFF)
val CreamVariant = Color(0xFFFFE8DC)

val InkDark = Color(0xFF2D1B3D)
val InkSoft = Color(0xFF6B5B75)

// Right and wrong answers - green and red, but softened so a mistake
// doesn't feel like an alarm going off.
val AnswerRight = Color(0xFF2ED9A0)
val AnswerWrong = Color(0xFFFF8B8B)

/** Gradient pairs, one per level, getting warmer as difficulty rises. */
val LevelGradients = listOf(
    listOf(Mint, Color(0xFF4DD9C0)),
    listOf(Sunshine, Color(0xFFFF9A5C)),
    listOf(Coral, Magenta)
)

fun gradientForLevel(level: Int): List<Color> =
    LevelGradients[(level - 1).coerceIn(0, LevelGradients.lastIndex)]

val HeroGradient = listOf(Coral, Magenta, PlumLight)
