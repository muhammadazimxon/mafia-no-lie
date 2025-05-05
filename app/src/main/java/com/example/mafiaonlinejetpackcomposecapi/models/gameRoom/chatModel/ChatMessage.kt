package com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.chatModel

data class ChatMessage(
    val id: Int,
    val playerName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
