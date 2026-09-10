package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestLogInData(
    @SerialName("id") val id: Int,
    @SerialName("isUserExists") val isUserExists: Boolean,
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("userDto") val userDto: UserDto
)
