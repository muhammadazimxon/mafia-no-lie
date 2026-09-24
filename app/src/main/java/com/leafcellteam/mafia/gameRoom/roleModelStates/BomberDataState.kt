package com.leafcellteam.mafia.gameRoom.roleModelStates

data class BomberDataState(
    val isBomberAbilityAvailable: Boolean = false,
    val bomberDialog: Boolean = false,
    val bomberChosenName: String = "",
    val bomberChosenId: Int = -1,
)