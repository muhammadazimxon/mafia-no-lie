package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("isGuest") val isGuest: Boolean
)
