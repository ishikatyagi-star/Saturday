package com.studysnap.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.studysnap.navigation.Routes
import com.studysnap.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(subjectId: String, viewModel: ChatbotViewModel = hiltViewModel()) {
    LaunchedEffect(subjectId) { viewModel.loadContext(subjectId) }
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) { if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ask StudySnap", color = TextPrimary) },
                navigationIcon = { IconButton(onClick = { viewModel.navBack() }) { Icon(Icons.Default.ArrowBack, "", tint = TextPrimary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                Modifier.weight(1f).padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg -> MessageBubble(msg) }
            }
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    placeholder = { Text("Ask anything...", color = TextHint) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary, unfocusedBorderColor = SurfaceVariant,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Surface, unfocusedContainerColor = Surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (input.isNotBlank()) { viewModel.sendMessage(input); input = "" }
                    })
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = { if (input.isNotBlank()) { viewModel.sendMessage(input); input = "" } }
                ) { Icon(Icons.Default.Send, "Send", tint = Primary) }
}
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFE)
@Composable
private fun ChatPreview() {
    StudySnapTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Ask StudySnap") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                MessageBubble(ChatbotViewModel.MessageDisplay("user", "What is Newton's First Law?", System.currentTimeMillis()))
                Spacer(Modifier.height(8.dp))
                MessageBubble(ChatbotViewModel.MessageDisplay("assistant", "Newton's First Law, also called the Law of Inertia, states that an object at rest stays at rest and an object in motion stays in motion unless acted upon by an unbalanced force.", System.currentTimeMillis()))
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Ask anything...") }, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = {}) { Icon(Icons.Default.Send, "Send", tint = Primary) }
                }
            }
        }
    }
}
    }
}

@Composable
@Composable
fun MessageBubble(message: ChatbotViewModel.MessageDisplay) {
    val isUser = message.role == "user"
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start) {
        Surface(
            color = if (isUser) Primary.copy(alpha = 0.1f) else SurfaceVariant,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(vertical = 2.dp).widthIn(max = 300.dp)
        ) {
            Text(message.content, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        }
    }
}