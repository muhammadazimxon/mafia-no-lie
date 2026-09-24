package com.leafcellteam.mafia.gameRoom.roleModelStates

data class BarmanDataState(
    val isBarmanAbilityAvailable: Boolean = false,
    val barmanDialog: Boolean = false,
    val barmanChosenName: String = "",
    val barmanChosenId: Int = -1,
)