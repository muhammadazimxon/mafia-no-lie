package com.leafcellteam.mafia.historyService.historyModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameParticipantData(
    @SerialName("PlayerName") val playerName : String,
)
