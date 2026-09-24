package com.leafcellteam.mafia.creatingSection.models

import com.leafcellteam.mafia.roles.Role

data class RolesChooserParam(
    val value: Boolean,
    val imageId: Int,
    val role: Role,
    val position: Int,
    val onCheckedChange: (Boolean) -> Unit,
    val helpButtonClick: (Int) -> Unit,
    val onClick: (RolesChooserParam) -> Boolean
)
