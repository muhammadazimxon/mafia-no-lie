package com.leafcellteam.shared.gameRoom.roleStates

data class DetectiveDataState(
    val isDetectiveAbilityAvailable: Boolean = false,
    val detectiveDialog: Boolean = false,
    val detectiveChosenName: String = "",
    val detectiveChosenId: Int = -1,
)
