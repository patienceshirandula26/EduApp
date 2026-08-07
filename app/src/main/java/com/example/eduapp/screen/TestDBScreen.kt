package com.example.eduapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eduapp.database.QuizResult
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestDBScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val results by viewModel.resultsFor("tester")
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Puzzles found in assets: ${viewModel.totalPuzzleCount()}")
        Text("Levels: ${viewModel.availableLevels()}")
        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(onClick = {
                viewModel.saveResult(
                    QuizResult(
                        username = "tester",
                        level = 1,
                        correct = (0..6).random(),
                        total = 6,
                        durationSeconds = 45
                    )
                )
            }) { Text("Add test result") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { viewModel.clearResults("tester") }) { Text("Clear") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(results) { r ->
                Text("Level ${r.level}: ${r.correct}/${r.total} in ${r.durationSeconds}s")
            }
        }
    }
}
