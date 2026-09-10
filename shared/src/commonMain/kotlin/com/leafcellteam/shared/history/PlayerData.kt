package com.leafcellteam.shared.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    @SerialName("id") val playerId: Int = 0,
    @SerialName("name") val playerName: String = "",
    @SerialName("allPlayedGames") val allPlayedGames: Int = 0,
    @SerialName("wonGames") val wonGames: Int = 0,
    @SerialName("winRate") val winRate: Int = 0
)
