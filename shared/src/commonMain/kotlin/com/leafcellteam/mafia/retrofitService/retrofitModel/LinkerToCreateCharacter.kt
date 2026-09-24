package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkerToCreateCharacter(
    @SerialName("id") val id : Int,
    @SerialName("userId") val userId : Int,
    @SerialName("key") val key : String,
    @SerialName("isCorrect") val isCorrect : Boolean
)