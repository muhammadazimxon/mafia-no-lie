package com.leafcellteam.shared.gameRoom.roleStates

data class CurrentPlayerDataState(
    val playerId: Int = -1,
    val playerName: String = "",
    val message: String = "",
    val isObserving: Boolean = false,
)
