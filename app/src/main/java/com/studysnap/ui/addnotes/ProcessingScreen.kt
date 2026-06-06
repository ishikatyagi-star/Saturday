package com.studysnap.ui.addnotes

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*
import kotlinx.coroutines.delay

sealed class ProcessingStep(val label: String) {
    object Ocr : ProcessingStep("Reading text from image")
    object Llm : ProcessingStep("Understanding concepts")
    object Store : ProcessingStep("Saving notes")
    object Done : ProcessingStep("Done!")
    data class Error(val msg: String) : ProcessingStep("Error")
}

@Composable
fun ProcessingScreen(
    navController: NavController,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val step by viewModel.currentStep.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startProcessing(context)
    }

    LaunchedEffect(step) {
        if (step is ProcessingStep.Done) {
            delay(800)
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val s = step) {
            is ProcessingStep.Ocr, is ProcessingStep.Llm, is ProcessingStep.Store -> {
                ProcessingAnimation(label = s.label)
            }
            is ProcessingStep.Done -> {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Accent, modifier = Modifier.size(72.dp))
                Spacer(Modifier.height(16.dp))
                Text("Notes processed!", style = MaterialTheme.typography.headlineMedium, color = Accent)
                Text("Returning home...", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
            is ProcessingStep.Error -> {
                Icon(Icons.Default.Error, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(72.dp))
                Spacer(Modifier.height(16.dp))
                Text(s.msg, style = MaterialTheme.typography.bodyLarge, color = ErrorRed, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) { Text("Go Back") }
            }
        }
    }
}

@Composable
private fun ProcessingAnimation(label: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(800), repeatMode = RepeatMode.Reverse)
    )
    CircularProgressIndicator(color = Primary.copy(alpha = alpha), modifier = Modifier.size(72.dp))
    Spacer(Modifier.height(20.dp))
    Text(label, style = MaterialTheme.typography.titleMedium, color = TextSecondary)
    Spacer(Modifier.height(8.dp))
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.6f), color = Primary, trackColor = Primary.copy(alpha = 0.1f))
}