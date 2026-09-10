package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: Int,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("isEmailVerified") val isEmailVerified: Boolean
)
