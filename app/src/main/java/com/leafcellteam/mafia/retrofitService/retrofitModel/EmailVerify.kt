package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerify(
    @SerializedName("Email") val email : String,
    @SerializedName("Code") val code : String
)