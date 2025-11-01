package com.example.mafiaonlinejetpackcomposecapi.joinRoom.models

data class RoomState(
    val roomsList: List<RoomData> = emptyList(),
    val isPasswordDialogOn: Boolean = false,
    val password: String = "",
    val showPassword: Boolean = false,
    val passwordError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val dialogsGuid: String = "",
    val correctPassword: String = "",
    val joinViaPasswordIndex: Int = 0
)