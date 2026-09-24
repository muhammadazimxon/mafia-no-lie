package com.leafcellteam.mafia.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * Раньше здесь был ещё Android 12+ "динамический цвет" (dynamicDarkColorScheme/
 * dynamicLightColorScheme) на основе обоев пользователя — это чисто android'овская
 * фича (нужен Context), аналога на iOS нет. Чтобы тема была одинаковой на всех
 * платформах, оставили обычную светлую/тёмную схему. При желании динамический
 * цвет можно вернуть отдельно только для Android через expect/actual.
 */
@Composable
fun MafiaOnlineJetpackComposeCAPITheme(
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
