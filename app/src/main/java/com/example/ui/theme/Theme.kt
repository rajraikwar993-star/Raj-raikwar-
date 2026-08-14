package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DarkBluePrimary,
    onPrimary = Color.White,
    primaryContainer = DarkNavySecondary,
    onPrimaryContainer = Color.White,
    secondary = OrangeAccent,
    onSecondary = Color.White,
    tertiary = OrangeVibrant,
    background = SoftBackground,
    onBackground = DarkBluePrimary,
    surface = Color.White,
    onSurface = DarkBluePrimary,
    error = EmergencyRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeVibrant,
    onPrimary = DarkBluePrimary,
    primaryContainer = DarkBluePrimary,
    onPrimaryContainer = Color.White,
    secondary = OrangeAccent,
    onSecondary = Color.White,
    tertiary = OrangeVibrant,
    background = DarkBackground,
    onBackground = SoftBackground,
    surface = DarkNavySecondary,
    onSurface = SoftBackground,
    error = EmergencyRed,
    onError = Color.White
)

@Composable
fun GaadiRentTheme(
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
