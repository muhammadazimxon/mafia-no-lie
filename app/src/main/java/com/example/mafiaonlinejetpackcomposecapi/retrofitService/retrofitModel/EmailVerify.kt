package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName


data class EmailVerify(
    @SerializedName("Email") val email : String,
    @SerializedName("Code") val code : String
)