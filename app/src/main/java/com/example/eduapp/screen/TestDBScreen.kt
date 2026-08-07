package com.example.eduapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestDBScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel()
) {
    val users by viewModel.results.collectAsStateWithLifecycle(initialValue = emptyList())
    var name by remember { mutableStateOf("") }

    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Puzzles found in assets: ${viewModel.totalPuzzleCount()}")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
                viewModel.addUser(name)
                name = ""
            }) { Text("Add User") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.clearUsers() }) { Text("Clear") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(users) { user ->
                Text("ID: ${user.id}, ${user.username}, score=${user.score}, level=${user.level}")
            }
        }
    }
}
