package com.example.eduapp.ui.theme

import androidx.compose.ui.graphics.Color

// Deep violet-blue "sky" with candy-bright buttons on top.

val Grape = Color(0xFF7C3AED)
val GrapeDeep = Color(0xFF5B21B6)
val GrapeNight = Color(0xFF2E1065)

val Sky = Color(0xFF3B82F6)
val SkyLight = Color(0xFF60A5FA)

val Teal = Color(0xFF14B8A6)
val TealLight = Color(0xFF5EEAD4)

val Leaf = Color(0xFF22C55E)
val LeafLight = Color(0xFF4ADE80)

val Gold = Color(0xFFFBBF24)
val GoldLight = Color(0xFFFDE68A)

val Bubblegum = Color(0xFFF472B6)

val Cloud = Color(0xFFF3F0FF)
val CloudCard = Color(0xFFFFFFFF)
val CloudVariant = Color(0xFFE4DDFF)

val Midnight = Color(0xFF1E1B4B)
val MidnightSoft = Color(0xFF6D6A9C)

val AnswerRight = Color(0xFF22C55E)
val AnswerWrong = Color(0xFFFB7185)

/** Sky gradient behind the hero areas. */
val SkyGradient = listOf(Grape, Sky, Teal)

/** One gradient per level, cool to warm as it gets harder. */
val LevelGradients = listOf(
    listOf(LeafLight, Leaf),
    listOf(SkyLight, Sky),
    listOf(Bubblegum, Grape)
)

fun gradientForLevel(level: Int): List<Color> =
    LevelGradients[(level - 1).coerceIn(0, LevelGradients.lastIndex)]
