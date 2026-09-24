package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileData(
    @SerialName("id") val id: Int = -1,
    @SerialName("name") val name: String = "",
    @SerialName("allPlayedGames") val allPlayedGames: Int = -1,
    @SerialName("wonGames") val wonGames: Int = -1,
    @SerialName("winRate") val winRate: Int = -1,
    @SerialName("playedQuantityAsCivilian") val playedQuantityAsCivilian: Int = -1,
    @SerialName("playedQuantityAsMafia") val playedQuantityAsMafia: Int = -1
)