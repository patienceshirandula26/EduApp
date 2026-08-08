package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("\uD83E\uDDE9", fontSize = 64.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "PicQuiz",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Look at the picture. Work out the number.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(36.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                error = null
            },
            label = { Text("What should we call you?") },
            supportingText = { error?.let { Text(it) } },
            isError = error != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                val trimmed = name.trim()
                when {
                    trimmed.isBlank() -> error = "Please enter a name"
                    trimmed.length > 20 -> error = "That's a bit long, try 20 letters or fewer"
                    else -> {
                        viewModel.setUsername(trimmed)
                        navController.navigate("landing") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Start playing")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Your name is only stored on this device. Nothing is sent anywhere.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}
