package com.leafcellteam.shared.gameRoom.models

data class PlayerDto(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: String = "",
    val avatarColor: List<Int> = emptyList()
)
