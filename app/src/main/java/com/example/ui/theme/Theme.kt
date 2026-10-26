package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MercaOrange,
    onPrimary = Color.White,
    primaryContainer = MercaOrangeLight,
    onPrimaryContainer = MercaOrangeDeep,
    secondary = FarmGreen,
    onSecondary = Color.White,
    secondaryContainer = FarmGreenLight,
    onSecondaryContainer = FarmGreenDark,
    tertiary = HarvestAmber,
    onTertiary = Color.Black,
    tertiaryContainer = HarvestAmberLight,
    background = WarmBackground,
    onBackground = TextDark,
    surface = WarmSurface,
    onSurface = TextDark,
    surfaceVariant = MercaOrangeContainer,
    onSurfaceVariant = TextMedium,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = MercaOrange,
    onPrimary = Color.White,
    primaryContainer = MercaOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = FarmGreen,
    onSecondary = Color.White,
    background = Color(0xFF181513),
    onBackground = Color(0xFFF3F4F6),
    surface = Color(0xFF231E1A),
    onSurface = Color(0xFFF3F4F6)
)

@Composable
fun MercaEasyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We prioritize the distinct Warm Market brand identity
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
