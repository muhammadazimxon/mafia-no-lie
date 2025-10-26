package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

data class LoginAsGuestResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)