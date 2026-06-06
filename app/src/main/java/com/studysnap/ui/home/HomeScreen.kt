package com.studysnap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.studysnap.data.entity.Subject
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_NOTES) },
                containerColor = Primary,
                contentColor = Color.White
            ) { Text("+", style = MaterialTheme.typography.headlineLarge) }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text("StudySnap", style = MaterialTheme.typography.displayLarge, color = Primary)
            Spacer(Modifier.height(4.dp))
            Text(greeting(), style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            Spacer(Modifier.height(32.dp))
            if (subjects.isEmpty()) EmptyHome { navController.navigate(Routes.ADD_NOTES) }
            else {
                Text("Your Subjects", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))
                subjects.forEach { s ->
                    SubjectBubble(subject = s, onClick = { navController.navigate(Routes.stories(s.id)) })
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptyHome(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No subjects yet", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, color = TextSecondary)
        Spacer(Modifier.height(8.dp))
        Text("Capture a screenshot or lecture notes to get started.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = TextHint)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Add Notes") }
    }
}

private fun greeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 0..11 -> "Good morning! Ready to learn?"
    in 12..16 -> "Good afternoon! Let's study."
    else -> "Good evening! Quick review?"
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFE)
@Composable
private fun HomeScreenPreview() {
    StudySnapTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {}, containerColor = Primary, contentColor = Color.White) {
                    Text("+", style = MaterialTheme.typography.headlineLarge)
                }
            }
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text("StudySnap", style = MaterialTheme.typography.displayLarge, color = Primary)
                Spacer(Modifier.height(4.dp))
                Text("Good morning! Ready to learn?", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                Spacer(Modifier.height(32.dp))
                Text("Your Subjects", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))
                SubjectBubble(subject = Subject("p1", "Physics — Newton's Laws", "seeder"), onClick = {})
                Spacer(Modifier.height(10.dp))
                SubjectBubble(subject = Subject("c1", "Chemistry — Organic", "ocr"), onClick = {})
            }
        }
    }
}