package com.leafcellteam.shared.waitingRoom.models

import com.leafcellteam.shared.gameRoom.models.Phase
import com.leafcellteam.shared.gameRoom.models.Player

data class ChatMessage(
    val playerId: Int,
    val user: String,
    val message: String,
    val timeStamp: Long = 0L,
    val phaseWhenSent: Phase,
    val currentPlayerWhenSent: Player?
)
