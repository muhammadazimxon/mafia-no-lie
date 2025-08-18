package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName

data class PlayerRegisterDataRequest(
    @SerializedName("Email") val email: String
)
