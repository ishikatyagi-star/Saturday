package com.studysnap.ui.stories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.studysnap.data.entity.Concept
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectStoriesScreen(
    subjectId: String,
    navController: NavController,
    viewModel: StoriesViewModel = hiltViewModel()
) {
    LaunchedEffect(subjectId) { viewModel.loadConcepts(subjectId) }
    val concepts by viewModel.concepts.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.subjectName.value, color = TextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (concepts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { concepts.size })
            Column(Modifier.fillMaxSize().padding(padding)) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                    ComicCard(
                        concept = concepts[page],
                        onQuizClick = { navController.navigate(Routes.quiz(concepts[page].id, subjectId)) },
                        onFlashcardClick = { navController.navigate(Routes.flashcards(concepts[page].id)) }
                    )
                }
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center) {
                    concepts.forEachIndexed { idx, _ ->
                        Box(
                            Modifier.size(if (idx == pagerState.currentPage) 10.dp else 6.dp)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(50))
                                .then(Modifier.background(if (idx == pagerState.currentPage) Primary else TextHint))
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ComicCard(concept: Concept, onQuizClick: () -> Unit, onFlashcardClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().weight(0.45f),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.08f))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (concept.comicImageBase64 != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data("data:image/png;base64,${concept.comicImageBase64}").build(),
                        contentDescription = concept.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(concept.title.take(1).uppercase(), style = MaterialTheme.typography.displayLarge, color = Primary.copy(alpha = 0.3f))
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(concept.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(concept.summary, style = MaterialTheme.typography.bodyLarge, color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        concept.keyPoints.split(",").take(3).forEach { point ->
            Row(Modifier.padding(vertical = 2.dp)) {
                Text("•  ", color = Accent)
                Text(point.trim(), style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("💡 ${concept.analogy}", style = MaterialTheme.typography.bodyMedium, color = TextHint, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onQuizClick, colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Quiz Me") }
            OutlinedButton(onClick = onFlashcardClick) { Text("Flashcards") }
        }
    }
}