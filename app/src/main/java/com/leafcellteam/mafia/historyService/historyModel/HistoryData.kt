package com.leafcellteam.mafia.historyService.historyModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryData(
    @SerializedName("gameId") val gameId : String = "",
    @SerializedName("name") val gameName : String = "",
    @SerializedName("wonSide") val wonSide : String = "",
    @SerializedName("duration") val duration : Int = 0,
    @SerializedName("date") val date : String,
    @SerializedName("isCurrentPlayerWon") val isCurrentPlayerWon : Boolean = false,
    @SerializedName("playerNames") val playerNames : List<String> = emptyList()
)