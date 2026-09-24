package com.leafcellteam.mafia.waitingSection.mvi

sealed class WaitingRoomEvent {
    data class UpdateMessage(val newMessage: String) : WaitingRoomEvent()
    data class SubmitMessage(val message: String) : WaitingRoomEvent()
}