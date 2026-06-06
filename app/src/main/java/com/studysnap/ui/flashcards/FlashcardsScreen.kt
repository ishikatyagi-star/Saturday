package com.studysnap.ui.flashcards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studysnap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(conceptId: String, viewModel: FlashcardsViewModel = hiltViewModel()) {
    LaunchedEffect(conceptId) { viewModel.loadCards(conceptId) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flashcards", color = TextPrimary) },
                navigationIcon = { IconButton(onClick = { viewModel.unload() }) { Icon(Icons.Default.ArrowBack, "", tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        when (val s = state) {
            is FlashcardState.loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
            is FlashcardState.empty -> EmptyScreen(Modifier.fillMaxSize().padding(padding).padding(32.dp), onGenerate = { viewModel.generateCards(conceptId) })
            is FlashcardState.active -> SwipeCard(card = s.card, onGotIt = { viewModel.swipeRight() }, onReviewLater = { viewModel.swipeLeft() })
            is FlashcardState.done -> DoneScreen(Modifier.fillMaxSize().padding(padding).padding(32.dp), onRestart = { viewModel.reload(conceptId) })
        }
    }
}

@Composable
private fun SwipeCard(card: FlashcardsViewModel.CardDisplay, onGotIt: () -> Unit, onReviewLater: () -> Unit) {
    var flipped by remember { mutableStateOf(false) }
    val bg by animateColorAsState(if (flipped) Secondary.copy(alpha = 0.08f) else Primary.copy(alpha = 0.08f))

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Swipe or tap", style = MaterialTheme.typography.labelLarge, color = TextHint)
        Spacer(Modifier.height(12.dp))

        Card(
            onClick = { flipped = !flipped },
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
            colors = CardDefaults.cardColors(containerColor = bg),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    if (!flipped) {
                        Text(card.front, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(8.dp))
                        Text(card.difficulty.uppercase(), style = MaterialTheme.typography.labelMedium,
                            color = when(card.difficulty) { "hard" -> ErrorRed; "medium" -> WarningOrange; else -> Accent })
                    } else {
                        Text(card.back, style = MaterialTheme.typography.bodyLarge, color = Secondary, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { flipped = false; onReviewLater() }, colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)) { Text("Review Later") }
            Button(onClick = { flipped = false; onGotIt() }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) { Text("Got It") }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier, onGenerate: () -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("No flashcards yet", style = MaterialTheme.typography.headlineMedium, color = TextSecondary)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onGenerate, colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
            Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text("Generate")
        }
    }
}

@Composable
fun DoneScreen(modifier: Modifier, onRestart: () -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("All done!", style = MaterialTheme.typography.headlineMedium, color = Accent, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("You've reviewed all flashcards.", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onRestart) { Text("Start Again") }
    }
}