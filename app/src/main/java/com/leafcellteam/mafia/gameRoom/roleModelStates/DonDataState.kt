package com.leafcellteam.mafia.gameRoom.roleModelStates

data class DonDataState(
    val isDonAbilityAvailable: Boolean = false,
    val donDialog: Boolean = false,
    val donChosenName: String = "",
    val donChosenId: Int = -1,
)
