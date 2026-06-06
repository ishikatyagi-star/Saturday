package com.hackathon.saturday.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Today : BottomNavItem("today", "Today", Icons.Default.Home)
    object Capture : BottomNavItem("capture", "Capture", Icons.Default.AddCircle)
    object Ask : BottomNavItem("ask", "Ask", Icons.Default.ChatBubble)
}
