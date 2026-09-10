package com.leafcellteam.shared.gameRoom.roleStates

data class BomberDataState(
    val isBomberAbilityAvailable: Boolean = false,
    val bomberDialog: Boolean = false,
    val bomberChosenName: String = "",
    val bomberChosenId: Int = -1,
)
