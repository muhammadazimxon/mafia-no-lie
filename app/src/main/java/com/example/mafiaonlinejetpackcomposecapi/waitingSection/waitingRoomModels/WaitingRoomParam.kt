package com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels

import androidx.compose.ui.Modifier

data class WaitingRoomParam(
    val modifier: Modifier,
    val nextRoom: () -> Unit,
    val onBack: () -> Unit,
)
