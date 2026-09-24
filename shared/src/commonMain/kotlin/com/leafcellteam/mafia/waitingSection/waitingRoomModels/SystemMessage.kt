package com.leafcellteam.mafia.waitingSection.waitingRoomModels

import kotlin.time.Clock
import kotlin.time.Instant


data class SystemMessage(
    val content: String,
    val temStamp: Instant = Clock.System.now()
)
