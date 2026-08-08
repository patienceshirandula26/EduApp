package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.data.WordState
import com.example.eduapp.ui.theme.ChunkyButton
import com.example.eduapp.ui.theme.GradientPanel
import com.example.eduapp.ui.theme.SkyGradient
import com.example.eduapp.ui.theme.Gold
import com.example.eduapp.ui.theme.GoldLight
import com.example.eduapp.ui.theme.Grape
import com.example.eduapp.ui.theme.GrapeDeep
import com.example.eduapp.ui.theme.GrapeNight
import com.example.eduapp.ui.theme.Midnight
import com.example.eduapp.ui.theme.Teal
import com.example.eduapp.ui.theme.TealLight
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val name = settings.username

    // remember() so a new Room query isn't built on every recomposition.
    val playedFlow = remember(name) { viewModel.quizzesPlayed(name) }
    val correctFlow = remember(name) { viewModel.totalCorrect(name) }
    val puzzleCount = remember { viewModel.totalPuzzleCount() }

    val played by playedFlow.collectAsStateWithLifecycle(initialValue = 0)
    val correct by correctFlow.collectAsStateWithLifecycle(initialValue = 0)
    val word by viewModel.wordOfDay.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("PicQuiz") }) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            GradientPanel(colours = SkyGradient, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Hi $name",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                    Text(
                        text = "$puzzleCount picture puzzles waiting",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "$played",
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White
                            )
                            Text(
                                "quizzes",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "$correct",
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White
                            )
                            Text(
                                "solved",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            ChunkyButton(
                text = "Start playing",
                onClick = { navController.navigate("levels") },
                modifier = Modifier.fillMaxWidth(),
                height = 68
            )

            ChunkyButton(
                text = "My progress",
                onClick = { navController.navigate("statistics") },
                modifier = Modifier.fillMaxWidth(),
                colours = listOf(GoldLight, Gold),
                baseColour = Color(0xFFB45309),
                contentColour = Midnight,
                height = 54
            )

            ChunkyButton(
                text = "Settings",
                onClick = { navController.navigate("setting") },
                modifier = Modifier.fillMaxWidth(),
                colours = listOf(TealLight, Teal),
                baseColour = Color(0xFF0F766E),
                contentColour = Midnight,
                height = 54
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("WORD OF THE DAY", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(8.dp))

                    when (val w = word) {
                        is WordState.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Text("  Loading...", style = MaterialTheme.typography.bodyMedium)
                        }

                        is WordState.Error -> Column {
                            Text(w.message, style = MaterialTheme.typography.bodyMedium)
                            TextButton(onClick = { viewModel.loadWordOfDay() }) {
                                Text("Try again")
                            }
                        }

                        is WordState.Success -> Column {
                            Text(
                                w.word.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(w.definition, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
