package com.leafcellteam.mafia.gameRoom.roleModelStates

data class SherifDataState(
    val isSherifAbilityAvailable: Boolean = false,
    val sherifDialog: Boolean = false,
    val sherifChosenName: String = "",
    val sherifChosenId: Int = -1,
)
