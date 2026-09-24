package com.leafcellteam.mafia.waitingSection.waitingRoomModels

import java.util.Date

data class SystemMessage(
    val content: String,
    val temStamp: Date = Date()
)
