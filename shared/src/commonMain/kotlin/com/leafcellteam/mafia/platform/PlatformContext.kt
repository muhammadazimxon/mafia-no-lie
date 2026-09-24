package com.leafcellteam.mafia.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal

expect abstract class PlatformContext

expect val LocalPlatformContext: CompositionLocal<PlatformContext>

@Composable
fun currentPlatformContext(): PlatformContext = LocalPlatformContext.current