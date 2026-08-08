package com.example.eduapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.eduapp.R

/** Baloo 2 - rounded and chunky, built for games and young readers. */
val Baloo = FontFamily(
    Font(R.font.baloo_regular, FontWeight.Normal),
    Font(R.font.baloo_semibold, FontWeight.SemiBold),
    Font(R.font.baloo_bold, FontWeight.Bold),
    Font(R.font.baloo_extrabold, FontWeight.ExtraBold)
)

val Typography = Typography(
    displayLarge = TextStyle(Baloo, FontWeight.ExtraBold, 54.sp, lineHeight = 62.sp),
    displayMedium = TextStyle(Baloo, FontWeight.ExtraBold, 44.sp, lineHeight = 52.sp),
    displaySmall = TextStyle(Baloo, FontWeight.ExtraBold, 34.sp, lineHeight = 42.sp),
    headlineLarge = TextStyle(Baloo, FontWeight.ExtraBold, 30.sp, lineHeight = 38.sp),
    headlineMedium = TextStyle(Baloo, FontWeight.Bold, 26.sp, lineHeight = 34.sp),
    headlineSmall = TextStyle(Baloo, FontWeight.Bold, 22.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(Baloo, FontWeight.Bold, 21.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(Baloo, FontWeight.SemiBold, 18.sp, lineHeight = 25.sp),
    bodyLarge = TextStyle(Baloo, FontWeight.Normal, 17.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(Baloo, FontWeight.Normal, 15.sp, lineHeight = 22.sp),
    bodySmall = TextStyle(Baloo, FontWeight.Normal, 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(Baloo, FontWeight.Bold, 15.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(Baloo, FontWeight.SemiBold, 12.sp, letterSpacing = 0.8.sp)
)

private fun TextStyle(
    family: FontFamily,
    weight: FontWeight,
    size: androidx.compose.ui.unit.TextUnit,
    lineHeight: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    letterSpacing: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified
) = TextStyle(
    fontFamily = family,
    fontWeight = weight,
    fontSize = size,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing
)
