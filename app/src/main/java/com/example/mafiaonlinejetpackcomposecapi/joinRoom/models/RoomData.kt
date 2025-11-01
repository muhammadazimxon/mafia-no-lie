package com.example.mafiaonlinejetpackcomposecapi.joinRoom.models

data class RoomData(
    val roomId: String,
    val roomName: String,
    val password: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playerQuantity: Int,
    val isClickedAlready: Boolean = false
)
