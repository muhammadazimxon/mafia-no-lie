package com.leafcellteam.shared.gameRoom.roleStates

data class DonDataState(
    val isDonAbilityAvailable: Boolean = false,
    val donDialog: Boolean = false,
    val donChosenName: String = "",
    val donChosenId: Int = -1,
)
