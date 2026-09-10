package com.leafcellteam.shared.gameRoom.roleStates

data class InformatorDataState(
    val isInformatorAbilityAvailable: Boolean = false,
    val informatorDialog: Boolean = false,
    val informatorChosenName: String = "",
    val informatorChosenId: Int = -1,
)
