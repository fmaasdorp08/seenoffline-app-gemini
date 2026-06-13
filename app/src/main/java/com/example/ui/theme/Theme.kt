package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BrightNeonGreen,
    secondary = SatinSlate,
    tertiary = PremiumGold,
    background = DeepCharcoal,
    surface = SatinSlate,
    onPrimary = DeepCharcoal,
    onSecondary = PureWhite,
    onTertiary = DeepCharcoal,
    onBackground = PureWhite,
    onSurface = PureWhite,
    outline = BorderSlate
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for luxury club aesthetic
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve branded A24 theme
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
