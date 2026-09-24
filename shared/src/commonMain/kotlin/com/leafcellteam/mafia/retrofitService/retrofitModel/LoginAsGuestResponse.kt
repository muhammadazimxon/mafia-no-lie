package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.Serializable

@Serializable
data class LoginAsGuestResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)