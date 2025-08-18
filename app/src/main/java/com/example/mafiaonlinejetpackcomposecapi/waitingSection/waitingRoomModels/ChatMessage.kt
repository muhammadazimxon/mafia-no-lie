package com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels

import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Phase
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player
import java.util.Date

data class ChatMessage(
    val playerId: Int,
    val user: String,
    val message: String,
    val timeStamp: Date = Date(),
    val phaseWhenSent: Phase,
    val currentPlayerWhenSent: Player?
)
