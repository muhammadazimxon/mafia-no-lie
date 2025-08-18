package com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels

import java.util.Date

data class SystemMessage(
    val content: String,
    val temStamp: Date = Date()
)
