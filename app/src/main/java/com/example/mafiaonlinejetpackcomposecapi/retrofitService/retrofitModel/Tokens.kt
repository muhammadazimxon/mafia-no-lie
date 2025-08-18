package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class Tokens(
    @SerializedName("accessToken") val accessToken : String,
    @SerializedName("refreshToken") val refreshToken : String,
    @SerializedName("userDto") val userDto : UserDto,
)