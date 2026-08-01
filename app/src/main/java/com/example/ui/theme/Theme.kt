package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SilaDarkPrimary,
    onPrimary = Color.White,
    primaryContainer = SilaSentBubbleDark,
    onPrimaryContainer = Color.White,
    secondary = SilaDarkSecondary,
    onSecondary = Color.Black,
    tertiary = SilaTertiary,
    background = SilaDarkBackground,
    onBackground = SilaDarkOnSurface,
    surface = SilaDarkSurface,
    onSurface = SilaDarkOnSurface,
    surfaceVariant = SilaDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8),
    error = SilaErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = SilaLightPrimary,
    onPrimary = Color.White,
    primaryContainer = SilaSentBubbleLight,
    onPrimaryContainer = SilaPrimaryVariant,
    secondary = SilaLightSecondary,
    onSecondary = Color.White,
    tertiary = SilaTertiary,
    background = SilaLightBackground,
    onBackground = SilaLightOnSurface,
    surface = SilaLightSurface,
    onSurface = SilaLightOnSurface,
    surfaceVariant = SilaLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF64748B),
    error = SilaErrorRed
)

@Composable
fun SilaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SilaTheme(darkTheme = darkTheme, content = content)
}

