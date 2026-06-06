package com.studysnap.ui.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*

@Composable
fun QuizScreen(conceptId: String, subjectId: String, navController: NavController, viewModel: QuizViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(conceptId) { viewModel.loadQuiz(conceptId, subjectId) }

    when (val s = state) {
        is QuizState.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
        is QuizState.failed -> Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(s.error, color = ErrorRed, textAlign = TextAlign.Center)
                Button(onClick = { navController.popBackStack() }) { Text("Back") }
            }
        }
        is QuizState.ready -> QuestionCard(q = s.question, onAnswer = { idx -> viewModel.answerQuestion(idx) })
        is QuizState.showResult -> {
            navController.navigate(Routes.quizResult(conceptId, subjectId, s.score, s.total)) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    }
}

@Composable
private fun QuestionCard(q: QuizViewModel.QuestionDisplay, onAnswer: (Int) -> Unit) {
    var selected by remember { mutableIntStateOf(-1) }
    var revealed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Q${q.index + 1}/${q.total}", style = MaterialTheme.typography.labelLarge, color = Primary)
        Spacer(Modifier.height(12.dp))
        Text(q.question, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        q.options.forEachIndexed { idx, opt ->
            val bg by animateColorAsState(
                when {
                    revealed && idx == q.correctIndex -> CorrectGreen.copy(alpha = 0.15f)
                    revealed && idx == selected && idx != q.correctIndex -> ErrorRed.copy(alpha = 0.1f)
                    idx == selected -> Primary.copy(alpha = 0.12f)
                    else -> SurfaceVariant
                }
            )
            Card(
                onClick = { if (!revealed) { selected = idx; q.isTF || onAnswer(idx); if (q.isTF) revealed = true } },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = bg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(opt, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (!revealed && !q.isTF) {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { if (selected >= 0) { revealed = true; onAnswer(selected) } },
                enabled = selected >= 0,
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) { Text("Check Answer") }
        }

        if (revealed) {
            Spacer(Modifier.height(16.dp))
            Text(q.explanation, style = MaterialTheme.typography.bodyLarge, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.nextQuestion(selected) }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                Text("Next")
            }
        }
    }
}