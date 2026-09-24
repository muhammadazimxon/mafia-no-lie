package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRegisterDataRequest(
    @SerializedName("Email") val email: String
)
