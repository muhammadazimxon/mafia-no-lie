package com.leafcellteam.shared.gameRoom.roleStates

data class LoverDataState(
    val isLoverAbilityAvailable: Boolean = false,
    val loverDialog: Boolean = false,
    val loverChosenName: String = "",
    val loverChosenId: Int = -1,
)
