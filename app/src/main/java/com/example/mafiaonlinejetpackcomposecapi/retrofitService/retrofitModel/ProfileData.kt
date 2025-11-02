package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("name") val name: String = "",
    @SerializedName("allPlayedGames") val allPlayedGames: Int = -1,
    @SerializedName("wonGames") val wonGames: Int = -1,
    @SerializedName("winRate") val winRate: Int = -1,
    @SerializedName("playedQuantityAsCivilian") val playedQuantityAsCivilian: Int = -1,
    @SerializedName("playedQuantityAsMafia") val playedQuantityAsMafia: Int = -1
)