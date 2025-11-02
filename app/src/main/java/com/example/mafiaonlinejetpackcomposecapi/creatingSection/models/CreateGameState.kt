package com.example.mafiaonlinejetpackcomposecapi.creatingSection.models

import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.roles.RoleData

data class CreateGameState(
    val roomName: String = "",
    val rangeOfPlayers: ClosedFloatingPointRange<Float> = 5f..15f,
    val password: String = "",
    val isNotCreated: Boolean = true,
    val isLoading: Boolean = false,
    val isShowPasswordState: Boolean = false,
    val helpButtonState: RoleData? = null,
    val isErrorRoomName: Boolean = false,
    val isDayPhase: Boolean = false,
    val isNightPhase: Boolean = true,

    val civilianRoles: List<RoleData> = listOf(
        RoleData(Role.Sherif),
        RoleData(Role.Detective),
        RoleData(Role.Doctor),
        RoleData(Role.Lover),
        RoleData(Role.Mimic),
        RoleData(Role.Journalist)
    ),
    val mafiaRoles: List<RoleData> = listOf(
        RoleData(Role.Barman),
        RoleData(Role.Informator),
        RoleData(Role.Bomber),
        RoleData(Role.Don)
    )
)