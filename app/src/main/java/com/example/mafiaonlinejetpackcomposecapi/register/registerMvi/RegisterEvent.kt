package com.example.mafiaonlinejetpackcomposecapi.register.registerMvi

sealed class RegisterEvent {
    data class UpdatePassword(val password : String) : RegisterEvent()
    data class UpdatePlayerName(val name : String) : RegisterEvent()
    data class UpdateRepeatPlayerName(val name : String) : RegisterEvent()
    data class OnUpdateRepeatPassword(val newPassword : String) : RegisterEvent()
    data object TogglePasswordVisibility : RegisterEvent()
    data object OnRepeatPasswordIconClick : RegisterEvent()
    data object OnDismiss : RegisterEvent()
    data class UpdateEmail(val email : String) : RegisterEvent()
    data class UpdateRepeatEmail(val email : String) : RegisterEvent()
    data class UpdateConfirmCode(val code : String) : RegisterEvent()
    data class OnEmailCheck(val onCheck : () -> Unit) : RegisterEvent()
    data class OnConfirmEmailByCode(val onConfirm : () -> Unit) : RegisterEvent()
    data class OnConfirmCreateNewCharacter(val onConfirmCreateNewCharacter : () -> Unit) : RegisterEvent()
    data object SendCodeAgain : RegisterEvent()
    data class OnLogIn(val onLogIn : () -> Unit) : RegisterEvent()
}