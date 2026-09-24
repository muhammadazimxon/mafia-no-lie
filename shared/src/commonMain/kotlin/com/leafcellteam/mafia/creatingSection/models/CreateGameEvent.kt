package com.leafcellteam.mafia.creatingSection.models

sealed class CreateGameEvent {
    data class UpdatePassword(val password: String) : CreateGameEvent()
    data class TogglePasswordVisibility(val isShowPasswordState: Boolean) : CreateGameEvent()
    data class UpdateRoomName(val roomName: String) : CreateGameEvent()
    data class UpdateRangeOfPlayers(val rangeOfPlayers: ClosedFloatingPointRange<Float>) : CreateGameEvent()
    data class OnChoseCivilianRole(val isChosen: Boolean, val clickedPosition: Int) : CreateGameEvent()
    data class OnChoseMafiaRole(val isChosen: Boolean, val clickedPosition: Int) : CreateGameEvent()
    data class CivilianHelpButton(val clickedPosition: Int) : CreateGameEvent()
    data class MafiaHelpButton(val clickedPosition: Int) : CreateGameEvent()
    data class CreateGameAction(val onCreate: () -> Unit, val country: String) : CreateGameEvent()
    data object DismissHelpButton : CreateGameEvent()
    data class ChangePhase(val isDay: Boolean, val isNight: Boolean) : CreateGameEvent()
}