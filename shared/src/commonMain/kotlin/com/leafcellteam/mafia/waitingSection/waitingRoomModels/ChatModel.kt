package com.leafcellteam.mafia.waitingSection.waitingRoomModels

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    val playerId: Int,
    val playerName: String,
    val message: String
)
