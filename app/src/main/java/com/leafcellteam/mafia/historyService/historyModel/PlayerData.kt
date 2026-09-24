package com.leafcellteam.mafia.historyService.historyModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    @SerializedName("id") val playerId : Int = 0,
    @SerializedName("name") val playerName : String = "",
    @SerializedName("allPlayedGames") val allPlayedGames : Int = 0,
    @SerializedName("wonGames") val wonGames : Int = 0,
    @SerializedName("winRate") val winRate : Int = 0
)