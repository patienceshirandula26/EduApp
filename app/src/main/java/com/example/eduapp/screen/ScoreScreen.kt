package com.example.eduapp.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreScreen(
    navController: NavHostController,
    level: Int,
    score: Int,
    total: Int,
    seconds: Int = 0,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val percentage = if (total > 0) (score * 100) / total else 0
    val isPerfect = total > 0 && score == total

    // LaunchedEffect(Unit) so the sound fires once, not on every recomposition.
    LaunchedEffect(Unit) { viewModel.playRoundFinishedSound(isPerfect) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (total > 0) score.toFloat() / total else 0f,
        animationSpec = tween(900),
        label = "scoreProgress"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = when {
                isPerfect -> "\uD83C\uDFC6"
                percentage >= 50 -> "\uD83C\uDF1F"
                else -> "\uD83E\uDDE9"
            },
            fontSize = 72.sp,
            modifier = Modifier.clearAndSetSemantics { }
        )

        Text(
            text = when {
                isPerfect -> "Every single one!"
                percentage >= 67 -> "Really well done"
                percentage >= 34 -> "Good effort"
                else -> "These ones were tricky"
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Level $level",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Finished in ${com.example.eduapp.model.TimeFormat.format(seconds)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$score / $total",
                    style = MaterialTheme.typography.displayMedium
                )
                Text("puzzles solved", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape)
                )
                Spacer(Modifier.height(6.dp))
                Text("$percentage%", style = MaterialTheme.typography.labelLarge)
            }
        }

        Text(
            text = if (isPerfect) "Try another level next."
            else "The puzzles shuffle every time, so another go will feel different.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate("game/$level") {
                    popUpTo("levels")
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Play this level again")
        }

        OutlinedButton(
            onClick = {
                navController.navigate("levels") { popUpTo("landing") }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Choose another level")
        }

        OutlinedButton(
            onClick = {
                navController.navigate("landing") {
                    popUpTo("landing") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Back home")
        }
    }
}
