package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RequestLogInData(
    @SerializedName("id") val id : Int,
    @SerializedName("isUserExists") val isUserExists : Boolean,
    @SerializedName("accessToken") val accessToken : String,
    @SerializedName("refreshToken") val refreshToken : String,
    @SerializedName("userDto") val userDto : UserDto
)
