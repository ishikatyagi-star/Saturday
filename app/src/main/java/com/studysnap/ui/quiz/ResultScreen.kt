package com.studysnap.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*

@Composable
fun ResultScreen(
    conceptId: String, subjectId: String, score: Int, total: Int,
    navController: NavController, viewModel: ResultViewModel = hiltViewModel()
) {
    LaunchedEffect(conceptId) { viewModel.loadConcept(conceptId) }
    val conceptTitle by viewModel.conceptTitle.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val pct = if (total > 0) (score * 100) / total else 0
        val color = when { pct >= 80 -> CorrectGreen; pct >= 50 -> WarningOrange; else -> ErrorRed }

        Text("Quiz Complete!", style = MaterialTheme.typography.displayLarge, color = Primary)
        Spacer(Modifier.height(8.dp))
        Text(conceptTitle, style = MaterialTheme.typography.titleLarge, color = TextSecondary)
        Spacer(Modifier.height(32.dp))

        Text("$score / $total", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = color)
        Text("$pct% correct", style = MaterialTheme.typography.titleMedium, color = color)
        Spacer(Modifier.height(8.dp))

        Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)), shape = MaterialTheme.shapes.medium) {
            Text(
                when { pct >= 80 -> "Great job! You've mastered this concept."; pct >= 50 -> "Good effort! Review the weak areas."; else -> "Keep studying! Retry this concept." },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Review Stories") }
            Button(onClick = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Home") }
        }
    }
}