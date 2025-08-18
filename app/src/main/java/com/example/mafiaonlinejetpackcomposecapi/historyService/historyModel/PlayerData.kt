package com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel

import com.google.gson.annotations.SerializedName

data class PlayerData(
    @SerializedName("id") val playerId : Int = 0,
    @SerializedName("name") val playerName : String = "",
    @SerializedName("allPlayedGames") val allPlayedGames : Int = 0,
    @SerializedName("wonGames") val wonGames : Int = 0,
    @SerializedName("winRate") val winRate : Int = 0
)