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

// Generous corners everywhere. This is a game, not a form.
private val PicQuizShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

private val LightColors = lightColorScheme(
    primary = CoralDeep,
    onPrimary = Color.White,
    primaryContainer = CreamVariant,
    onPrimaryContainer = Plum,
    secondary = Sunshine,
    onSecondary = Plum,
    secondaryContainer = SunshineSoft,
    onSecondaryContainer = Plum,
    tertiary = Mint,
    onTertiary = Plum,
    tertiaryContainer = MintSoft,
    onTertiaryContainer = Plum,
    background = CreamBg,
    onBackground = InkDark,
    surface = CreamCard,
    onSurface = InkDark,
    surfaceVariant = CreamVariant,
    onSurfaceVariant = InkSoft,
    outline = Color(0xFFE0C4B8)
)

private val DarkColors = darkColorScheme(
    primary = Coral,
    onPrimary = Plum,
    primaryContainer = PlumLight,
    onPrimaryContainer = Color.White,
    secondary = Sunshine,
    onSecondary = Plum,
    secondaryContainer = Color(0xFF6B4A1F),
    onSecondaryContainer = SunshineSoft,
    tertiary = Mint,
    onTertiary = Plum,
    tertiaryContainer = Color(0xFF1B5C48),
    onTertiaryContainer = MintSoft,
    background = Plum,
    onBackground = Color(0xFFF5EDF5),
    surface = PlumLight,
    onSurface = Color(0xFFF5EDF5),
    surfaceVariant = Color(0xFF3D2650),
    onSurfaceVariant = Color(0xFFD9C4E0),
    outline = Color(0xFF6B4A7C)
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
