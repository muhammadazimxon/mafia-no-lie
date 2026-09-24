package com.leafcellteam.mafia.historyService.historyModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GameParticipantData(
    @SerializedName("PlayerName") val playerName : String,
)
