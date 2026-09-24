package com.leafcellteam.mafia.waitingSection.waitingRoomModels

import com.leafcellteam.mafia.gameRoom.models.Phase
import com.leafcellteam.mafia.gameRoom.models.Player
import java.util.Date

data class ChatMessage(
    val playerId: Int,
    val user: String,
    val message: String,
    val timeStamp: Date = Date(),
    val phaseWhenSent: Phase,
    val currentPlayerWhenSent: Player?
)
