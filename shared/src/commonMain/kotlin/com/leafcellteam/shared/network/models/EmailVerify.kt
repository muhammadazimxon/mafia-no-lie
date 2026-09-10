package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerify(
    @SerialName("Email") val email: String,
    @SerialName("Code") val code: String
)
