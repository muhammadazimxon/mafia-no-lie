package com.leafcellteam.shared.network.models

data class LoginAsGuestResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)
