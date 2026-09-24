package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("isGuest") val isGuest: Boolean
)