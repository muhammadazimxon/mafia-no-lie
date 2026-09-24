package com.leafcellteam.mafia.gameRoom.models

data class AlivePlayersTransmission(
    val playerName: String,
    val playerId: Int,
    val isAlive: Boolean
)