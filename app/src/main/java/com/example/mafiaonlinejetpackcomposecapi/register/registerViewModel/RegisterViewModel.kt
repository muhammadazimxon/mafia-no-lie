package com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafiaonlinejetpackcomposecapi.R
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.viewModel.AchievementsViewModel
import com.example.mafiaonlinejetpackcomposecapi.interseptor.TokenManager
import com.example.mafiaonlinejetpackcomposecapi.register.data.RAToken
import com.example.mafiaonlinejetpackcomposecapi.register.registerMvi.RegisterEvent
import com.example.mafiaonlinejetpackcomposecapi.register.registerMvi.RegisterState
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.EmailVerify
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.LinkerToCreateCharacter
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.LoginRequest
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.PlayerRegisterDataRequest
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val waitingRoomViewModel: WaitingRoomViewModel,
    private val achievementsViewModel: AchievementsViewModel,
    private val tokenManager: TokenManager
) : ViewModel() {

    private var _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private var _raToken = MutableStateFlow(RAToken())
    val raToken: StateFlow<RAToken> = _raToken.asStateFlow()

    var currentPlayerId by mutableIntStateOf(-1)
    var isGuest by mutableStateOf(false)

    fun authValidation(userId: Int, email: String, name: String) {
        tokenManager.saveIfGuest(false)
        isGuest = false
        _registerState.value = _registerState.value.copy(
            email = email,
            repeatEmail = email,
            playerName = name,
            repeatPlayerName = name
        )
        waitingRoomViewModel.currentPlayer = _registerState.value.playerName
        currentPlayerId = userId
        waitingRoomViewModel.changePlayerID(userId)
    }

    fun authValidationAsGuest(userId: Int, name: String, accessToken: String, refreshToken: String) {
        tokenManager.saveIfGuest(true)
        isGuest = true
        _registerState.value = _registerState.value.copy(
            playerName = name,
            repeatPlayerName = name
        )
        waitingRoomViewModel.currentPlayer = name
        currentPlayerId = userId
        waitingRoomViewModel.changePlayerID(userId)
        achievementsViewModel.changePlayerId(userId)

        _raToken.value = _raToken.value.copy(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        tokenManager.saveTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun changeAchievementId(userId: Int) {
        achievementsViewModel.changePlayerId(userId)
    }

    fun registerEventHandler(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.TogglePasswordVisibility -> {
                if (_registerState.value.passwordVisualization == VisualTransformation.None) {
                    _registerState.value = _registerState.value.copy(
                        passwordVisualization = PasswordVisualTransformation(),
                        passwordIcon = R.drawable.visibility_off
                    )
                } else {
                    _registerState.value = _registerState.value.copy(
                        passwordVisualization = VisualTransformation.None,
                        passwordIcon = R.drawable.visibile
                    )
                }
            }

            is RegisterEvent.UpdatePassword -> {
                _registerState.value = _registerState.value.copy(password = event.password)
                if( _registerState.value.isWrongInput ) _registerState.value = _registerState.value.copy(isWrongInput = false)
            }

            is RegisterEvent.UpdatePlayerName -> {
                _registerState.value = _registerState.value.copy(playerName = event.name)
                if( _registerState.value.isWrongInput ) _registerState.value = _registerState.value.copy(isWrongInput = false)
            }

            is RegisterEvent.OnLogIn -> {
                val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
                if (_registerState.value.password.isBlank() || _registerState.value.email.isBlank()) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Please fill all fields\nPlease check carefully and try again",
                        isTextMessageOn = true
                    )
                    return
                } else if (emailRegex.matches(_registerState.value.email).not()) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Please write a valid email\nPlease check carefully and try again",
                        isTextMessageOn = true
                    )
                    return
                } else if(_registerState.value.password.length < 6){
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Password must contain at least 6 any symbols or digits\nPlease check it carefully and try again",
                        isTextMessageOn = true
                    )
                    return
                }

                viewModelScope.launch {
                    try {
                        val emailData = MafiaApi.retrofitService.getEmailExists(
                            LoginRequest(
                                email = _registerState.value.email,
                                password = _registerState.value.password
                            )
                        )

                        if (emailData.isUserExists.not()) {
                            _registerState.value = _registerState.value.copy(
                                textMessage = "This email does not exists\nPlease check and try again!",
                                isTextMessageOn = true
                            )
                        } else {
                            currentPlayerId = emailData.id
                            _raToken.value = _raToken.value.copy(
                                accessToken = emailData.accessToken,
                                refreshToken = emailData.refreshToken
                            )

                            tokenManager.saveTokens(
                                accessToken = _raToken.value.accessToken,
                                refreshToken = _raToken.value.refreshToken
                            )

                            _registerState.value = _registerState.value.copy(
                                email = emailData.userDto.email,
                                repeatEmail = emailData.userDto.email,
                                playerName = emailData.userDto.name,
                                repeatPlayerName = emailData.userDto.name
                            )

                            waitingRoomViewModel.currentPlayer = _registerState.value.playerName
                            achievementsViewModel.changePlayerId(currentPlayerId)
                            waitingRoomViewModel.changePlayerID(currentPlayerId)
                            event.onLogIn()
                        }
                    } catch (e: Exception) {
                        Log.d("LogIn", "on Log In Click\n${e.message}")
                        _registerState.value = _registerState.value.copy(
                            isTextMessageOn = true,
                            textMessage = "Email or password wrong please try again"
                        )
                    }
                }
            }

            is RegisterEvent.OnRepeatPasswordIconClick -> {
                if ( _registerState.value.repeatPasswordVisualization == VisualTransformation.None ) {
                    _registerState.value = _registerState.value.copy(
                        repeatPasswordVisualization = PasswordVisualTransformation(),
                        repeatPasswordIcon = R.drawable.visibility_off
                    )
                } else {
                    _registerState.value = _registerState.value.copy(
                        repeatPasswordVisualization = VisualTransformation.None,
                        repeatPasswordIcon = R.drawable.visibile
                    )
                }
            }

            is RegisterEvent.OnUpdateRepeatPassword -> {
                _registerState.value = _registerState.value.copy(
                    repeatPassword = event.newPassword,
                    isTextMessageOn = false
                )
            }

            is RegisterEvent.OnDismiss -> {
                _registerState.value = _registerState.value.copy(isTextMessageOn = false)
            }

            is RegisterEvent.UpdateEmail -> {
                _registerState.value = _registerState.value.copy(email = event.email)
            }

            is RegisterEvent.UpdateRepeatEmail -> {
                _registerState.value = _registerState.value.copy(repeatEmail = event.email)
            }

            is RegisterEvent.UpdateConfirmCode -> {
                _registerState.value = _registerState.value.copy(confirmCode = event.code)
            }

            is RegisterEvent.OnEmailCheck -> {
                val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
                if (/*_registerState.value.email != _registerState.value.repeatEmail || */emailRegex.matches(_registerState.value.email).not()) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Email written incorrectly",
                        isTextMessageOn = true
                    )
                } else {
                    viewModelScope.launch {
                        try {
                            _registerState.value = _registerState.value.copy(isSpamWarning = true)
                            currentPlayerId = MafiaApi.retrofitService.register(
                                request = PlayerRegisterDataRequest(email = _registerState.value.email)
                            )
                            event.onCheck()

                        } catch (e: Exception) {
                            Log.d("EmailCheck", "registerEventHandler: ${e.message}")
                            _registerState.value = _registerState.value.copy(
                                textMessage = "Something went wrong! Error: ${e.message}",
                                isTextMessageOn = true
                            )
                        }
                    }
                }
            }

            is RegisterEvent.OnConfirmEmailByCode -> {
                if (_registerState.value.confirmCode.trim().length != 6) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Code must contain 6 digits",
                        isTextMessageOn = true,
                        confirmCode = ""
                    )
                    return
                }

                var emailVerification: LinkerToCreateCharacter? = null

                viewModelScope.launch {
                    try {
                        emailVerification = MafiaApi.retrofitService.verifyEmail(
                            EmailVerify(
                                email = _registerState.value.email,
                                code = _registerState.value.confirmCode
                            )
                        )
                        if (emailVerification!!.key.isNotEmpty() && emailVerification!!.userId != 0) {
                            _registerState.value = _registerState.value.copy(
                                keyToCreateCharacter = emailVerification!!.key
                            )
                            event.onConfirm()
                        }
                    } catch (e: Exception) {
                        Log.d("Register", "registerEventHandler: ${e.message}")
                        _registerState.value = _registerState.value.copy(
                            textMessage = "Something went wrong",
                            isTextMessageOn = true
                        )
                    }
                }
            }

            is RegisterEvent.UpdateRepeatPlayerName -> {
                _registerState.value = _registerState.value.copy(repeatPlayerName = event.name)
            }

            is RegisterEvent.OnConfirmCreateNewCharacter -> {
                if( _registerState.value.playerName.count { it.isLetter() } < 2 ) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Nickname must contain at least 2 letters, please check carefully and try again",
                        isTextMessageOn = true
                    )
                }
                else if( _registerState.value.password.length < 6 ) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Password must contain at least 6 symbols or digits or letters, please check carefully and try again",
                        isTextMessageOn = true
                    )
                } else if ( _registerState.value.playerName.length < 2 || _registerState.value.password != _registerState.value.repeatPassword ) {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Nickname does not contain at least 3 characters or password does not match with repeat password\nPlease check carefully",
                        isTextMessageOn = true
                    )
                } else if(/*_registerState.value.playerName == _registerState.value.repeatPlayerName &&*/ _registerState.value.password == _registerState.value.repeatPassword ) {
                    waitingRoomViewModel.currentPlayer = _registerState.value.playerName
                    viewModelScope.launch {
                        MafiaApi.retrofitService.createCharacter(
                            key = _registerState.value.keyToCreateCharacter,
                            email = _registerState.value.email,
                            password = _registerState.value.password,
                            name = _registerState.value.playerName
                        )
                    }
                    event.onConfirmCreateNewCharacter()
                    _registerState.value = _registerState.value.copy(
                        email = "",
                        repeatEmail = "",
                        password = "",
                        repeatPassword = ""
                    )
                } else {
                    _registerState.value = _registerState.value.copy(
                        textMessage = "Something might went wrong",
                        isTextMessageOn = true
                    )
                }
                return
            }

            is RegisterEvent.SendCodeAgain -> {
                viewModelScope.launch {
                    MafiaApi.retrofitService.reSendRegisterCode(
                        request = PlayerRegisterDataRequest(
                            email = _registerState.value.email
                        )
                    )
                }

                _registerState.value = _registerState.value.copy(
                    isReadyToSendCode = false,
                    sendCodeMessage = "${_registerState.value.sendCodeCount}"
                )

                viewModelScope.launch {
                    while( _registerState.value.sendCodeCount > 0 ) {
                        delay(1000)
                        _registerState.value = _registerState.value.copy (sendCodeCount = _registerState.value.sendCodeCount - 1)
                        _registerState.value = _registerState.value.copy (sendCodeMessage = _registerState.value.sendCodeCount.toString())
                    }

                    _registerState.value = _registerState.value.copy(
                        isReadyToSendCode = true,
                        sendCodeCount = 10,
                        sendCodeMessage = "Send code again"
                    )
                }
            }
            is RegisterEvent.OnSpamWarningDismiss -> {
                _registerState.value = _registerState.value.copy(isSpamWarning = false)
            }
        }
    }
    fun callForGuestDialog() {
        _registerState.value = _registerState.value.copy(
            textMessage = "Guests cannot view history and achievements.\nIf you would like to see them register please!",
            isTextMessageOn = true
        )
    }
}
