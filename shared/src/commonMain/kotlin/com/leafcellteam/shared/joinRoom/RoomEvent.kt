package com.leafcellteam.shared.joinRoom

sealed class RoomEvent {
    data class OnJoin(val guid: String, val onJoin: () -> Unit, val index: Int) : RoomEvent()
    data object OnUpdate : RoomEvent()
    data class OnPasswordDialogOpen(val correctPass: String, val guid: String, val index: Int) : RoomEvent()
    data class OnRandom(val onJoin: () -> Unit) : RoomEvent()
    data class UpdatePassword(val newPassword: String) : RoomEvent()
    data object TogglePasswordVisibility : RoomEvent()
    data object Dismiss : RoomEvent()
    data class JoinWithPassword(val guid: String, val onJoin: () -> Unit, val joinViaPasswordIndex: Int) : RoomEvent()
}
