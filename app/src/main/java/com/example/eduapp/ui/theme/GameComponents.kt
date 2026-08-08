package com.example.eduapp.ui.theme

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A chunky game button with a solid "base" underneath, so pressing it looks
 * like the cap is being pushed down onto the base. Standard mobile-game feel.
 */
@Composable
fun ChunkyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colours: List<Color> = listOf(Grape, GrapeDeep),
    baseColour: Color = GrapeNight,
    contentColour: Color = Color.White,
    height: Int = 62,
    enabled: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val drop by animateDpAsState(
        targetValue = if (pressed) 0.dp else { 6.dp },
        animationSpec = spring(),
        label = "buttonDrop"
    )

    Box(modifier = modifier.height((height + 6).dp)) {
        // The base, always sitting 6dp lower.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .offset(y = 6.dp)
                .clip(RoundedCornerShape(50))
                .background(baseColour.copy(alpha = if (enabled) 1f else 0.3f))
        )

        Surface(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interaction,
            shape = RoundedCornerShape(50),
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .offset(y = (6.dp - drop))
        ) {
            Box(
                modifier = Modifier.background(Brush.verticalGradient(colours)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    color = contentColour,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

/** Rounded gradient panel used for hero headers and level cards. */
@Composable
fun GradientPanel(
    colours: List<Color>,
    modifier: Modifier = Modifier,
    corner: Int = 28,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(corner.dp))
            .background(Brush.linearGradient(colours))
    ) {
        content()
    }
}
