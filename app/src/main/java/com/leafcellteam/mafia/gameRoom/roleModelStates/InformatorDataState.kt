package com.leafcellteam.mafia.gameRoom.roleModelStates

data class InformatorDataState(
    val isInformatorAbilityAvailable: Boolean = false,
    val informatorDialog: Boolean = false,
    val informatorChosenName: String = "",
    val informatorChosenId: Int = -1,
)