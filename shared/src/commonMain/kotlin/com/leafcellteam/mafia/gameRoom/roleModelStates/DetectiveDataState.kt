package com.leafcellteam.mafia.gameRoom.roleModelStates

data class DetectiveDataState(
    val isDetectiveAbilityAvailable: Boolean = false,
    val detectiveDialog: Boolean = false,
    val detectiveChosenName: String = "",
    val detectiveChosenId: Int = -1,
)