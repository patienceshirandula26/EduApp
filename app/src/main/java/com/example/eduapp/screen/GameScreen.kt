package com.example.eduapp.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.model.AnswerOptions
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavHostController,
    level: Int,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    // Shuffled once per round so the order differs every time you play.
    val puzzles = remember(level) { viewModel.puzzlesForLevel(level).shuffled() }

    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    val startedAt = remember(level) { System.currentTimeMillis() }

    if (puzzles.isEmpty()) {
        Text("No puzzles found for level $level")
        return
    }

    val puzzle = puzzles[index]
    val options = remember(puzzle.id) { AnswerOptions.optionsFor(puzzle.answer) }
    val imageState by rememberAssetImage(puzzle.assetPath)
    val image = imageState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Puzzle ${index + 1} of ${puzzles.size}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { (index + 1f) / puzzles.size },
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )

            Text("Score: $score", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (image != null) {
                        Image(
                            bitmap = image!!,
                            contentDescription = "Picture puzzle ${index + 1}, level $level",
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }

            Text(
                text = "What number replaces the question mark?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            options.chunked(2).forEach { rowOptions ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowOptions.forEach { option ->
                        val isCorrect = option == puzzle.answer
                        val colour by animateColorAsState(
                            targetValue = when {
                                selected == null -> MaterialTheme.colorScheme.secondaryContainer
                                isCorrect -> Color(0xFF23C16B)
                                option == selected -> Color(0xFFE5484D)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            label = "optionColour"
                        )

                        Button(
                            onClick = {
                                if (selected == null) {
                                    selected = option
                                    if (isCorrect) {
                                        score++
                                        viewModel.playCorrectSound()
                                    } else {
                                        viewModel.playWrongSound()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(64.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = colour,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("$option", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                }
            }

            if (selected != null) {
                Text(
                    text = if (selected == puzzle.answer) "Correct!"
                    else "The answer was ${puzzle.answer}",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {
                        if (index < puzzles.lastIndex) {
                            index++
                            selected = null
                        } else {
                            val seconds = ((System.currentTimeMillis() - startedAt) / 1000).toInt()
                            viewModel.saveQuizResult(level, score, puzzles.size, seconds)
                            navController.navigate("score/$level/$score/${puzzles.size}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(if (index < puzzles.lastIndex) "Next puzzle" else "See results")
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
