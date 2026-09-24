package com.leafcellteam.mafia.platform

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // На iOS системной кнопки "назад" нет — обработка нативного жеста
    // происходит на уровне UINavigationController/UIViewController, а не
    // здесь. Оставлено пустым намеренно.
}
