package com.example.eduapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Very round. Game buttons are pills, not rectangles.
private val PicQuizShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp)
)

private val LightColors = lightColorScheme(
    primary = Grape,
    onPrimary = Color.White,
    primaryContainer = CloudVariant,
    onPrimaryContainer = GrapeNight,
    secondary = Gold,
    onSecondary = Midnight,
    secondaryContainer = GoldLight,
    onSecondaryContainer = Midnight,
    tertiary = Teal,
    onTertiary = Color.White,
    tertiaryContainer = TealLight,
    onTertiaryContainer = Midnight,
    background = Cloud,
    onBackground = Midnight,
    surface = CloudCard,
    onSurface = Midnight,
    surfaceVariant = CloudVariant,
    onSurfaceVariant = MidnightSoft,
    outline = Color(0xFFC4B8F0)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA78BFA),
    onPrimary = GrapeNight,
    primaryContainer = GrapeDeep,
    onPrimaryContainer = Color.White,
    secondary = Gold,
    onSecondary = Midnight,
    secondaryContainer = Color(0xFF78500A),
    onSecondaryContainer = GoldLight,
    tertiary = TealLight,
    onTertiary = Midnight,
    tertiaryContainer = Color(0xFF0F766E),
    onTertiaryContainer = TealLight,
    background = GrapeNight,
    onBackground = Color(0xFFEDE9FE),
    surface = Color(0xFF3B2A6B),
    onSurface = Color(0xFFEDE9FE),
    surfaceVariant = Color(0xFF44337A),
    onSurfaceVariant = Color(0xFFCFC4F5),
    outline = Color(0xFF7C6BB5)
)

@Composable
fun EduAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = PicQuizShapes,
        content = content
    )
}
