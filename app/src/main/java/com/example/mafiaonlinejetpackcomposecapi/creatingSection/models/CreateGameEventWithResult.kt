package com.example.mafiaonlinejetpackcomposecapi.creatingSection.models

sealed class CreateGameEventWithResult {
    data class IsRoleBalanceOK(val rolesChooserParam: RolesChooserParam) : CreateGameEventWithResult()
}