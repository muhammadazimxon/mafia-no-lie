package com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.playerModels

import androidx.compose.ui.graphics.Color
import com.example.mafiaonlinejetpackcomposecapi.roles.Role

data class Player(
    val playerId: Int,
    val playerName: String,
    val role: Role,
    val avatarColor: Color,
    val isAlive: Boolean
)
