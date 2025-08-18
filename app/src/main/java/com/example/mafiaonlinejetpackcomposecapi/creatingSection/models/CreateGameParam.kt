package com.example.mafiaonlinejetpackcomposecapi.creatingSection.models

import androidx.compose.ui.Modifier

data class CreateGameParam(
    val modifier: Modifier,
    val onCreate: () -> Unit,
    val onBack: () -> Unit,
)
