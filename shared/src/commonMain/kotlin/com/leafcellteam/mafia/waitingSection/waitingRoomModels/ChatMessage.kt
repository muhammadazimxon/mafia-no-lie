package com.leafcellteam.mafia.waitingSection.waitingRoomModels

import com.leafcellteam.mafia.gameRoom.models.Phase
import com.leafcellteam.mafia.gameRoom.models.Player
import kotlin.time.Clock
import kotlin.time.Instant


data class ChatMessage(
    val playerId: Int,
    val user: String,
    val message: String,
    val timeStamp: Instant = Clock.System.now(),
    val phaseWhenSent: Phase,
    val currentPlayerWhenSent: Player?
)
