package com.leafcellteam.shared.gameRoom.models

data class PlayerInfo(
    val playerId: Int,
    val isAlive: Boolean,
    val isUnderLover: Boolean,
    val isUnderDoctor: Boolean,
    val isUnderJournalist: Boolean,
    val isUnderDetective: Boolean,
    val isUnderInformator: Boolean,
    val isDonSearch: Boolean
)
