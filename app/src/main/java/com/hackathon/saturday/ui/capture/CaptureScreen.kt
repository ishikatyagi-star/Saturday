package com.hackathon.saturday.ui.capture

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackathon.saturday.ui.components.CaptureButton
import com.hackathon.saturday.ui.components.ExtractedItemRow
import com.hackathon.saturday.ui.theme.*

@Composable
fun CaptureScreen(viewModel: CaptureViewModel = viewModel(factory = CaptureViewModel.Factory)) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.processImage(context, it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.cameraUri?.let { viewModel.processImage(context, it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Capture",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Take a photo or pick a screenshot. Saturday will extract deadlines, tasks, and events.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CaptureButton(onClick = {
                val uri = viewModel.createImageUri(context)
                viewModel.cameraUri = uri
                cameraLauncher.launch(uri)
            })
            FloatingActionButton(
                onClick = { galleryLauncher.launch("image/*") },
                containerColor = CardDark,
                contentColor = TextPrimary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val state = uiState) {
            is CaptureUiState.Loading -> {
                CircularProgressIndicator(color = AccentCyan)
                Text("Reading your image...", color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
            }
            is CaptureUiState.Success -> {
                Text("Extracted ${state.result.tasks.size + state.result.deadlines.size + state.result.events.size + state.result.flashcards.size} items", color = SuccessGreen)
                Spacer(modifier = Modifier.height(12.dp))
                state.result.tasks.forEach {
                    ExtractedItemRow(it.title, it.description ?: "", "TASK")
                }
                state.result.deadlines.forEach {
                    ExtractedItemRow(it.title, it.description ?: "", "DEADLINE")
                }
                state.result.events.forEach {
                    ExtractedItemRow(it.title, it.description ?: "", "EVENT")
                }
                state.result.flashcards.forEach {
                    ExtractedItemRow(it.question, it.answer, "FLASHCARD")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.saveToToday() },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = DeepSpace)
                ) {
                    Text("Save to Today")
                }
            }
            is CaptureUiState.Error -> {
                Text(state.message, color = UrgentRed)
            }
            else -> {}
        }
    }
}
