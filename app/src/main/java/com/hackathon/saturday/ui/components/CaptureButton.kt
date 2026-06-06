package com.hackathon.saturday.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackathon.saturday.ui.theme.AccentCyan
import com.hackathon.saturday.ui.theme.DeepSpace

@Composable
fun CaptureButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = AccentCyan,
        contentColor = DeepSpace,
        modifier = modifier.size(64.dp)
    ) {
        Icon(Icons.Default.CameraAlt, contentDescription = "Capture", modifier = Modifier.size(28.dp))
    }
}
