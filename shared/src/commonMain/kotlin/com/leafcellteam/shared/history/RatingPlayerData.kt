package com.leafcellteam.shared.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingPlayerData(
    @SerialName("winRate") val winRate: Int = 0,
    @SerialName("name") val name: String = ""
)
