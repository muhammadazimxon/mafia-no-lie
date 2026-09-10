package com.leafcellteam.shared.waitingRoom.models

data class WaitingRoomParam(
    val nextRoom: () -> Unit,
    val onBack: () -> Unit,
)
