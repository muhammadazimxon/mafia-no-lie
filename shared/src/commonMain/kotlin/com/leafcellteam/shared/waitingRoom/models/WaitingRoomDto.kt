package com.leafcellteam.shared.waitingRoom.models

data class WaitingRoomDto(
    val roomId: String,
    val roomName: String,
    val players: List<String>,
    val minPlayers: Int,
    val maxPlayers: Int,
    val chosenRoles: List<String>
)
