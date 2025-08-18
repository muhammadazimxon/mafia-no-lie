package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("RefreshToken") val refreshToken : String
)