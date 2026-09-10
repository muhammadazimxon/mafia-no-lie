package com.leafcellteam.shared.waitingRoom.mvi

sealed class WaitingRoomEvent {
    data class UpdateMessage(val newMessage: String) : WaitingRoomEvent()
    data class SubmitMessage(val message: String) : WaitingRoomEvent()
}
