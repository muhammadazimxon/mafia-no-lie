package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerify(
    @SerialName("Email") val email : String,
    @SerialName("Code") val code : String
)