package com.leafcellteam.mafia.gameRoom.models

import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.roles.Role

data class Player(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: Role = Role.Civilian,
    val avatarColor: Color = Color.Unspecified,
)