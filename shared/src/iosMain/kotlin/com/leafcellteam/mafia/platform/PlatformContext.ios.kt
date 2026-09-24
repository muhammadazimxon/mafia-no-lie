package com.leafcellteam.mafia.platform

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

actual abstract class PlatformContext
private val IosPlatformContext = object : PlatformContext() {}

actual val LocalPlatformContext: CompositionLocal<PlatformContext> =
    staticCompositionLocalOf { IosPlatformContext }
