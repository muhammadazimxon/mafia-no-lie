package com.leafcellteam.mafia.platform

import androidx.compose.runtime.CompositionLocal
import androidx.compose.ui.platform.LocalContext
import android.content.Context

actual typealias PlatformContext = Context
actual val LocalPlatformContext: CompositionLocal<PlatformContext> = LocalContext
