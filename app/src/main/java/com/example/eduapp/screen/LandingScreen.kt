package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
            Text(
                text = "Hi ${settings.username}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "There are $puzzleCount picture puzzles waiting.",
                style = MaterialTheme.typography.bodyLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$played", style = MaterialTheme.typography.headlineMedium)
                        Text("quizzes played", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$correct", style = MaterialTheme.typography.headlineMedium)
                        Text("puzzles solved", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Button(
                onClick = { navController.navigate("levels") },
                modifier = Modifier.fillMaxWidth().height(64.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.height(8.dp))
                Text("  Start playing", style = MaterialTheme.typography.titleLarge)
            }

            FilledTonalButton(
                onClick = { navController.navigate("setting") },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Text("  Settings")
            }

            FilledTonalButton(
                onClick = { navController.navigate("statistics") },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("My progress")
            }
        }
    }
}
