package com.leafcellteam.mafia.creatingSection.models

sealed class CreateGameEventWithResult {
    data class IsRoleBalanceOK(val rolesChooserParam: RolesChooserParam) : CreateGameEventWithResult()
}