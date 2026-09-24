package com.leafcellteam.mafia.historyService.historyModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryData(
    @SerialName("gameId") val gameId : String = "",
    @SerialName("name") val gameName : String = "",
    @SerialName("wonSide") val wonSide : String = "",
    @SerialName("duration") val duration : Int = 0,
    @SerialName("date") val date : String,
    @SerialName("isCurrentPlayerWon") val isCurrentPlayerWon : Boolean = false,
    @SerialName("playerNames") val playerNames : List<String> = emptyList()
)