package com.leafcellteam.shared.gameRoom.roleStates

data class DoctorDataState(
    val isDoctorAbilityAvailable: Boolean = false,
    val doctorDialog: Boolean = false,
    val doctorChosenName: String = "",
    val doctorChosenId: Int = -1,
)
