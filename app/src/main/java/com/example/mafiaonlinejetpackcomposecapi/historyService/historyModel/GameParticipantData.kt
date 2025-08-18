package com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel

import com.google.gson.annotations.SerializedName

data class GameParticipantData(
    @SerializedName("PlayerName") val playerName : String,
)
