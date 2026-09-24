package com.leafcellteam.mafia.register.registerMvi

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.DrawableResource
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.visibility_off

data class RegisterState(
    val password: String = "",
    val passwordVisualization: VisualTransformation = PasswordVisualTransformation(),
    val passwordIcon: DrawableResource = Res.drawable.visibility_off,
    val isWrongInput: Boolean = false,
    val playerName: String = "",
    val repeatPlayerName: String = "",
//    val mainButtonText: String = "Log In",
    val mainButtonText: String = "Log In",
    val secondaryButtonText: String = "Register",
    val repeatPassword: String = "",
    val textMessage: String = "",
    val isTextMessageOn: Boolean = false,
    val repeatPasswordVisualization: VisualTransformation = PasswordVisualTransformation(),
    val repeatPasswordIcon: DrawableResource = Res.drawable.visibility_off,
    val email: String = "",
    val repeatEmail: String = "",
    val confirmCode: String = "",
    var isReadyToSendCode: Boolean = true,
    var sendCodeMessage: String = "Send code again",
    var sendCodeCount: Int = 35,
    var keyToCreateCharacter: String = "",
    val isEmailConfirmButtonEnabled: Boolean = true,
    val isSpamWarning: Boolean = false
)