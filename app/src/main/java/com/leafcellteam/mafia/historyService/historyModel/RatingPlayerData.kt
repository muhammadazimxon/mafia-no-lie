package com.leafcellteam.mafia.historyService.historyModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RatingPlayerData(
    @SerializedName("winRate") val winRate : Int = 0,
    @SerializedName("name") val name : String = ""
)