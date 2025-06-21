package com.muhammetkonukcu.litlounge.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = darkColorScheme(
    primary = Black,
    background = White,
    tertiary = Neutral500,
    secondary = Neutral800,
    onBackground = Neutral200,
    tertiaryContainer = Neutral300
)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    background = Black,
    tertiary = Neutral500,
    secondary = Neutral200,
    onBackground = Neutral800,
    tertiaryContainer = Neutral550
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography(),
        content = content,
    )
}