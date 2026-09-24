package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LinkerToCreateCharacter(
    @SerializedName("id") val id : Int,
    @SerializedName("userId") val userId : Int,
    @SerializedName("key") val key : String,
    @SerializedName("isCorrect") val isCorrect : Boolean
)