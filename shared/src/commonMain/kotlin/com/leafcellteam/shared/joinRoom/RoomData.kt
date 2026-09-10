package com.leafcellteam.shared.joinRoom

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
