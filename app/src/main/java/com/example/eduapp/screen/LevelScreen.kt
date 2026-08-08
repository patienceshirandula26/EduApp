package com.example.eduapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.eduapp.ui.theme.GradientPanel
import com.example.eduapp.ui.theme.gradientForLevel
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val levels = remember { viewModel.availableLevels() }

    val bestFlows = remember(settings.username, levels) {
        levels.associateWith { viewModel.bestForLevel(settings.username, it) }
    }
    val counts = remember(levels) {
        levels.associateWith { viewModel.puzzleCountForLevel(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose a level") },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FilledTonalButton(
                onClick = { navController.navigate("game/${viewModel.randomLevel()}") },
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Icon(Icons.Default.Casino, contentDescription = null)
                Text("  Surprise me - random level")
            }

            Spacer(Modifier.height(4.dp))

            levels.forEach { level ->
                val best by bestFlows.getValue(level)
                    .collectAsStateWithLifecycle(initialValue = 0)

                LevelCard(
                    level = level,
                    puzzleCount = counts.getValue(level),
                    best = best,
                    onClick = { navController.navigate("game/$level") }
                )
            }
        }
    }
}

@Composable
private fun LevelCard(
    level: Int,
    puzzleCount: Int,
    best: Int,
    onClick: () -> Unit
) {
    val label = when (level) {
        1 -> "Warm up"
        2 -> "Getting trickier"
        3 -> "Brain bender"
        else -> "Level $level"
    }

    GradientPanel(
        colours = gradientForLevel(level),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$level",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                }
            }
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "$puzzleCount puzzles" +
                        if (best > 0) "  •  best $best/$puzzleCount" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
