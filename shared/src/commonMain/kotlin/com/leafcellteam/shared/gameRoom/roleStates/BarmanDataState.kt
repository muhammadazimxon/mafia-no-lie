package com.leafcellteam.shared.gameRoom.roleStates

data class BarmanDataState(
    val isBarmanAbilityAvailable: Boolean = false,
    val barmanDialog: Boolean = false,
    val barmanChosenName: String = "",
    val barmanChosenId: Int = -1,
)
