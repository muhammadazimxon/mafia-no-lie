package com.leafcellteam.shared.gameRoom.roleStates

data class SherifDataState(
    val isSherifAbilityAvailable: Boolean = false,
    val sherifDialog: Boolean = false,
    val sherifChosenName: String = "",
    val sherifChosenId: Int = -1,
)
