package com.leafcellteam.shared.gameRoom.models

import com.leafcellteam.shared.roles.Role

data class Player(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: Role = Role.Civilian,
    val avatarColorArgb: Long = 0xFF4CAF50L, // default green
)
