package com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel

import com.google.gson.annotations.SerializedName

data class RatingPlayerData(
    @SerializedName("winRate") val winRate : Int = 0,
    @SerializedName("name") val name : String = ""
)