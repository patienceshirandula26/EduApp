package com.example.eduapp.screen

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.eduapp.model.TimeFormat
import com.example.eduapp.ui.theme.AnswerRight
import com.example.eduapp.ui.theme.AnswerWrong
import com.example.eduapp.ui.theme.ChunkyButton
import com.example.eduapp.ui.theme.GradientPanel
import com.example.eduapp.ui.theme.Grape
import com.example.eduapp.ui.theme.GrapeDeep
import com.example.eduapp.ui.theme.GrapeNight
import com.example.eduapp.ui.theme.Midnight
import com.example.eduapp.ui.theme.gradientForLevel
import com.example.eduapp.viewmodel.AppViewModel
import kotlin.random.Random
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavHostController,
    level: Int,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    // Saving the seed rather than the list means the same shuffle is rebuilt
    // after a rotation, so index still points at the same puzzle.
    val seed by rememberSaveable(level) { mutableLongStateOf(System.nanoTime()) }
    val puzzles = remember(level, seed) {
        viewModel.puzzlesForLevel(level).shuffled(Random(seed))
    }

    var index by rememberSaveable(level) { mutableIntStateOf(0) }
    var score by rememberSaveable(level) { mutableIntStateOf(0) }
    var selected by rememberSaveable(level) { mutableStateOf<Int?>(null) }
    val startedAt by rememberSaveable(level) { mutableLongStateOf(System.currentTimeMillis()) }
    var elapsed by rememberSaveable(level) { mutableIntStateOf(0) }

    LaunchedEffect(level, selected) {
        while (selected == null) {
            delay(1_000)
            elapsed = ((System.currentTimeMillis() - startedAt) / 1000).toInt()
        }
    }

    if (puzzles.isEmpty()) {
        Text("No puzzles found for level $level")
        return
    }

    val puzzle = puzzles[index]
    val options = remember(puzzle.id, seed) {
        AnswerOptions.optionsFor(puzzle.answer, Random(seed + puzzle.id.hashCode()))
    }
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

            GradientPanel(
                colours = gradientForLevel(level),
                modifier = Modifier.fillMaxWidth(),
                corner = 20
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Score $score",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        TimeFormat.format(elapsed),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowOptions.forEach { option ->
                        val isCorrect = option == puzzle.answer
                        val revealed = selected != null

                        val colours = when {
                            !revealed -> listOf(Grape, GrapeDeep)
                            isCorrect -> listOf(AnswerRight, Color(0xFF16A34A))
                            option == selected -> listOf(AnswerWrong, Color(0xFFE11D48))
                            else -> listOf(Color(0xFFD4CCF0), Color(0xFFBFB4E8))
                        }

                        val base = when {
                            !revealed -> GrapeNight
                            isCorrect -> Color(0xFF14532D)
                            option == selected -> Color(0xFF881337)
                            else -> Color(0xFF9C90C8)
                        }

                        ChunkyButton(
                            text = "$option",
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
                            modifier = Modifier.weight(1f),
                            colours = colours,
                            baseColour = base,
                            contentColour = if (revealed && !isCorrect && option != selected)
                                Midnight else Color.White,
                            height = 66,
                            enabled = selected == null
                        )
                    }
                }
            }

            if (selected != null) {
                Text(
                    text = if (selected == puzzle.answer) "Correct!"
                    else "The answer was ${puzzle.answer}",
                    style = MaterialTheme.typography.titleMedium
                )

                ChunkyButton(
                    text = if (index < puzzles.lastIndex) "Next puzzle" else "See results",
                    onClick = {
                        if (index < puzzles.lastIndex) {
                            index++
                            selected = null
                        } else {
                            val seconds = ((System.currentTimeMillis() - startedAt) / 1000).toInt()
                            viewModel.saveQuizResult(level, score, puzzles.size, seconds)
                            navController.navigate("score/$level/$score/${puzzles.size}/$seconds")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    height = 58
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
