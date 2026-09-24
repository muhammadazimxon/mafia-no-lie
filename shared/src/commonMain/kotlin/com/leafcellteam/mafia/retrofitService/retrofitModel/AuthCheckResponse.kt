package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.Serializable

@Serializable
data class AuthCheckResponse(
    val message: String,
    val userId: String,
    val email: String,
    val userName: String,
    val isAuthenticated: Boolean,
    val isGuest: Boolean
)