package com.leafcellteam.mafia.joinRoom.models

import kotlinx.serialization.Serializable

@Serializable
data class RoomData(
    val roomId: String,
    val roomName: String,
    val password: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playerQuantity: Int,
    val country: String,
    val isClickedAlready: Boolean = false
)
