package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("isGuest") val isGuest: Boolean
)