package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id : Int,
    @SerializedName("email") val email : String,
    @SerializedName("name") val name : String,
    @SerializedName("isEmailVerified") val isEmailVerified : Boolean
)