package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("Email") val email: String,
    @SerialName("Password") val password: String
)
