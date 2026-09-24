package com.leafcellteam.mafia.platform

import androidx.compose.runtime.Composable

/**
 * androidx.activity.compose.BackHandler завязан на ComponentActivity и
 * недоступен в commonMain. На iOS аппаратной кнопки "назад" нет (жест
 * "смахнуть с края экрана" обрабатывается на уровне UINavigationController,
 * а не отдельным экраном), поэтому там это осознанный no-op.
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
