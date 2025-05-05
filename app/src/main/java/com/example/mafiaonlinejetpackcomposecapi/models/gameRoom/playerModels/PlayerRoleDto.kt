package com.example.mafiaonlinejetpackcomposecapi.models.gameRoom.playerModels

data class PlayerRoleDto(
    val playerId: Int,
    val playerName: String,
    val role: String,
    val avatarColor: List<Int>,
    val isAlive: Boolean
)
