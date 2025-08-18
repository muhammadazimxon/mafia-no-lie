package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class LinkerToCreateCharacter(
    @SerializedName("id") val id : Int,
    @SerializedName("userId") val userId : Int,
    @SerializedName("key") val key : String,
    @SerializedName("isCorrect") val isCorrect : Boolean
)