package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id : Int,
    @SerialName("email") val email : String,
    @SerialName("name") val name : String,
    @SerialName("isEmailVerified") val isEmailVerified : Boolean
)