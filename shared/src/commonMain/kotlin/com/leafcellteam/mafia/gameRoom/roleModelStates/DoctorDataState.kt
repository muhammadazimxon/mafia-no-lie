package com.leafcellteam.mafia.gameRoom.roleModelStates

data class DoctorDataState(
    val isDoctorAbilityAvailable: Boolean = false,
    val doctorDialog: Boolean = false,
    val doctorChosenName: String = "",
    val doctorChosenId: Int = -1,
)