package com.leafcellteam.shared.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRegisterDataRequest(
    @SerialName("Email") val email: String
)
