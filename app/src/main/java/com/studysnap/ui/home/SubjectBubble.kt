package com.studysnap.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studysnap.data.entity.Subject
import com.studysnap.ui.theme.*

@Composable
fun SubjectBubble(subject: Subject, onClick: () -> Unit) {
    val colorIndex = kotlin.math.abs(subject.id.hashCode()) % subjectColors.size
    val bubbleColor = subjectColors[colorIndex]

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).then(Modifier.background(bubbleColor.copy(alpha = 0.15f))), contentAlignment = Alignment.Center) {
                Text(subject.name.take(2).uppercase(), color = bubbleColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(subject.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Tap to explore", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}