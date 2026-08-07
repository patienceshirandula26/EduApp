package com.example.eduapp.helper

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun loadAssetImage(context: Context, path: String): ImageBitmap? =
    withContext(Dispatchers.IO) {
        runCatching {
            context.assets.open(path).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
        }.getOrNull()
    }

/**
 * Loads the puzzle image off the main thread. Returns null while loading,
 * so the UI can show a spinner instead of freezing.
 */
@Composable
fun rememberAssetImage(path: String): State<ImageBitmap?> {
    val context = LocalContext.current
    val state = remember(path) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(path) { state.value = loadAssetImage(context, path) }
    return state
}
