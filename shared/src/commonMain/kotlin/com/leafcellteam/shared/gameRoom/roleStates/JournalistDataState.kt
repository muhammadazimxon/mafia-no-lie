package com.leafcellteam.shared.gameRoom.roleStates

data class JournalistDataState(
    val isJournalistAbilityAvailable: Boolean = false,
    val journalistDialog: Boolean = false,
    val journalistChosenName: String = "",
    val firstJournalistChosenId: Int = -1,
    val secondJournalistChosenId: Int = -1,
    val journalistTempId: Int = -1,
)
