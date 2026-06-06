package com.hackathon.saturday.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackathon.saturday.ui.theme.AccentCyan
import com.hackathon.saturday.ui.theme.CardDark
import com.hackathon.saturday.ui.theme.TextPrimary
import com.hackathon.saturday.ui.theme.TextSecondary

@Composable
fun ExtractedItemRow(title: String, detail: String, type: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = type, style = MaterialTheme.typography.labelSmall, color = AccentCyan)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            if (detail.isNotBlank()) {
                Text(text = detail, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}
