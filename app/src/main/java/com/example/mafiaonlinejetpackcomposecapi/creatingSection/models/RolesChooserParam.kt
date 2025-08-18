package com.example.mafiaonlinejetpackcomposecapi.creatingSection.models

import com.example.mafiaonlinejetpackcomposecapi.roles.Role

data class RolesChooserParam(
    val value: Boolean,
    val imageId: Int,
    val role: Role,
    val position: Int,
    val onCheckedChange: (Boolean) -> Unit,
    val helpButtonClick: (Int) -> Unit,
    val onClick: (RolesChooserParam) -> Boolean
)
