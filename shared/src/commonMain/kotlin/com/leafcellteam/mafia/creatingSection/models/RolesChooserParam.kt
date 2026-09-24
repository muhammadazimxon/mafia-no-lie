package com.leafcellteam.mafia.creatingSection.models

import com.leafcellteam.mafia.roles.Role
import org.jetbrains.compose.resources.DrawableResource

data class RolesChooserParam(
    val value: Boolean,
    val image: DrawableResource,
    val role: Role,
    val position: Int,
    val onCheckedChange: (Boolean) -> Unit,
    val helpButtonClick: (Int) -> Unit,
    val onClick: (RolesChooserParam) -> Boolean
)
