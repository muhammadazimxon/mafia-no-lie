package com.leafcellteam.shared.network.models

data class AuthCheckResponse(
    val message: String,
    val userId: String,
    val email: String,
    val userName: String,
    val isAuthenticated: Boolean,
    val isGuest: Boolean
)
