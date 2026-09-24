package com.leafcellteam.mafia

import androidx.compose.ui.window.ComposeUIViewController
import com.leafcellteam.mafia.platform.LocalPlatformContext
import platform.UIKit.UIViewController

/**
 * Точка входа для iOS-приложения.
 *
 * Возвращает UIKit-driven UIViewController, который хостит общее Compose-приложение
 * (SharedApp()). Из Swift это вызывается как `MainViewController_iosKt.MainViewController()`.
 *
 * initApp() вызывается здесь один раз, до отрисовки первого экрана,
 * чтобы проинициализировать TokenPreferences и NetworkModule.
 */
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        val context = LocalPlatformContext.current
        initApp(context)
        SharedApp()
    }