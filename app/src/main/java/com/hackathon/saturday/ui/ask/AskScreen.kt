package com.hackathon.saturday.ui.ask

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hackathon.saturday.ui.theme.*

@Composable
fun AskScreen() {
    var input by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Ask Saturday",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            text = "Your campus assistant. Ask about deadlines, events, or anything.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(msg)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Ask anything...", color = TextSecondary) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentCyan,
                    unfocusedBorderColor = CardDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (input.isNotBlank()) {
                        messages.add(Message(input, true))
                        messages.add(Message(generateReply(input), false))
                        input = ""
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        messages.add(Message(input, true))
                        messages.add(Message(generateReply(input), false))
                        input = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = AccentCyan)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) AccentCyan.copy(alpha = 0.15f) else CardDark,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isUser) AccentCyan else TextPrimary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

private fun generateReply(input: String): String {
    val lower = input.lowercase()
    return when {
        lower.contains("deadline") || lower.contains("due") ->
            "I found 2 upcoming deadlines: CS201 Assignment (June 8) and Physics Lab Report (June 10). Want me to add reminders?"
        lower.contains("event") || lower.contains("session") ->
            "Next event: Hackathon Workshop on June 7 at 2 PM in the Innovation Lab."
        lower.contains("task") || lower.contains("todo") ->
            "You have 3 pending tasks today. The highest priority is submitting your DBMS project."
        lower.contains("hello") || lower.contains("hi") ->
            "Hey! I'm Saturday, your campus assistant. What do you need help with today?"
        else -> "I'm still learning, but I can help you track deadlines, tasks, and campus events. Try asking about what's due next!"
    }
}

data class Message(val text: String, val isUser: Boolean)
