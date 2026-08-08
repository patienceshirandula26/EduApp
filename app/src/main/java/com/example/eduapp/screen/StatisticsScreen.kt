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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.database.QuizResult
import com.example.eduapp.model.TimeFormat
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val name = settings.username

    val played by remember(name) { viewModel.quizzesPlayed(name) }
        .collectAsStateWithLifecycle(initialValue = 0)
    val correct by remember(name) { viewModel.totalCorrect(name) }
        .collectAsStateWithLifecycle(initialValue = 0)
    val attempted by remember(name) { viewModel.totalAttempted(name) }
        .collectAsStateWithLifecycle(initialValue = 0)
    val best by remember(name) { viewModel.bestScore(name) }
        .collectAsStateWithLifecycle(initialValue = 0)
    val avgTime by remember(name) { viewModel.averageDuration(name) }
        .collectAsStateWithLifecycle(initialValue = 0.0)
    val fastest by remember(name) { viewModel.fastestRound(name) }
        .collectAsStateWithLifecycle(initialValue = 0)
    val history by remember(name) { viewModel.resultsFor(name) }
        .collectAsStateWithLifecycle(initialValue = emptyList())

    var confirmClear by remember { mutableStateOf(false) }
    val accuracy = if (attempted > 0) (correct * 100) / attempted else 0

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Clear your progress?") },
            text = { Text("This deletes every quiz you've finished. It can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearMyResults()
                    confirmClear = false
                }) { Text("Delete everything") }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Keep it") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your progress") },
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
            if (played == 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "You haven't finished a quiz yet. Play one and your progress shows up here.",
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
                return@Column
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBox("Quizzes", "$played", Modifier.weight(1f))
                StatBox("Solved", "$correct", Modifier.weight(1f))
                StatBox("Accuracy", "$accuracy%", Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBox("Best round", "$best correct", Modifier.weight(1f))
                StatBox("Average time", TimeFormat.format(avgTime.toInt()), Modifier.weight(1f))
                StatBox("Fastest", TimeFormat.format(fastest), Modifier.weight(1f))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Best round", style = MaterialTheme.typography.labelLarge)
                    Text("$best correct", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Text("Recent quizzes", style = MaterialTheme.typography.titleLarge)

            history.take(15).forEach { result -> HistoryRow(result) }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { confirmClear = true },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Clear my progress")
            }

            Text(
                text = "All of this is stored only on this device.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun HistoryRow(result: QuizResult) {
    val formatter = remember { SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Level ${result.level}: ${result.correct}/${result.total}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "took ${result.durationSeconds}s",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                formatter.format(Date(result.playedAt)),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
