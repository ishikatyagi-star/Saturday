package com.hackathon.saturday.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hackathon.saturday.data.model.TodayItem
import com.hackathon.saturday.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodayCard(item: TodayItem, onComplete: () -> Unit = {}) {
    val (title, subtitle, date, typeLabel, priority) = when (item) {
        is TodayItem.TodayTask ->
            Quadruple(item.task.title, item.task.description, item.task.dueDate, "TASK", item.task.priority)
        is TodayItem.TodayDeadline ->
            Quadruple(item.deadline.title, item.deadline.description, item.deadline.dueDate, "DEADLINE", item.deadline.priority)
        is TodayItem.TodayEvent ->
            Quadruple(item.event.title, item.event.description, item.event.eventDate, "EVENT", 0)
        is TodayItem.TodayFlashcard ->
            Quadruple(item.flashcard.question, item.flashcard.answer, null, "FLASHCARD", 0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    if (priority > 0) {
                        PriorityBadge(priority)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                date?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(it),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
            if (item is TodayItem.TodayTask || item is TodayItem.TodayDeadline) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { onComplete() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SuccessGreen,
                        uncheckedColor = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: Int) {
    val (text, color) = when (priority) {
        2 -> "URGENT" to UrgentRed
        1 -> "HIGH" to HighOrange
        else -> "NORMAL" to TextSecondary
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault()).format(Date(timestamp))
}

data class Quadruple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
