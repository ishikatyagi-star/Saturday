package com.studysnap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val StudySnapColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Surface,
    primaryContainer = Primary.copy(alpha = 0.12f),
    secondary = Secondary,
    onSecondary = Surface,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = Surface,
    outline = TextHint
)

@Composable
fun StudySnapTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = StudySnapColorScheme,
        typography = Typography,
        content = content
    )
}