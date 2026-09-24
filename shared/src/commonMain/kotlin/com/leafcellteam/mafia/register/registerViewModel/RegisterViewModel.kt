package com.leafcellteam.mafia.register.registerViewModel
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.pleaseFillAllFields
import com.leafcellteam.mafia.resources.pleaseCheckCarefullyAndTryAgain
import com.leafcellteam.mafia.resources.pleaseWriteAValidEmail
import com.leafcellteam.mafia.resources.passwordMustContain
import com.leafcellteam.mafia.resources.emailDoesNotExist
import com.leafcellteam.mafia.resources.pleaseCheckAndTryAgain
import com.leafcellteam.mafia.resources.emailOrPasswordWrongPleaseTryAgain
import com.leafcellteam.mafia.resources.emailWrittenIncorrectly
import com.leafcellteam.mafia.resources.checkInBoxSpam
import com.leafcellteam.mafia.resources.somethingWentWrong
import com.leafcellteam.mafia.resources.error
import com.leafcellteam.mafia.resources.codeMustContain
import com.leafcellteam.mafia.resources.nicknameMustContain
import com.leafcellteam.mafia.resources.nicknameOrPasswordAreNotUnderTerm
import com.leafcellteam.mafia.resources.sendCodeAgain
import com.leafcellteam.mafia.resources.guestsCannotViewAchievements
import com.leafcellteam.mafia.resources.pleaseRegisterIfYouWantToSeeAchievements
import com.leafcellteam.mafia.resources.visibility_off
import com.leafcellteam.mafia.resources.visibile
import org.jetbrains.compose.resources.getString
import com.leafcellteam.mafia.achievementsSection.viewModel.AchievementsViewModel
import com.leafcellteam.mafia.register.data.RAToken
import com.leafcellteam.mafia.register.registerMvi.RegisterEvent
import com.leafcellteam.mafia.register.registerMvi.RegisterState
import com.leafcellteam.mafia.retrofitService.retrofitModel.EmailVerify
import com.leafcellteam.mafia.retrofitService.retrofitModel.LinkerToCreateCharacter
import com.leafcellteam.mafia.retrofitService.retrofitModel.LoginRequest
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.retrofitService.retrofitModel.PlayerRegisterDataRequest
import com.leafcellteam.mafia.tokenManager.TokenManager
import com.leafcellteam.mafia.tokenPreferences
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val waitingRoomViewModel: WaitingRoomViewModel,
    private val achievementsViewModel: AchievementsViewModel,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private var _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private var _raToken = MutableStateFlow(RAToken())
    val raToken: StateFlow<RAToken> = _raToken.asStateFlow()

    var currentPlayerId by mutableIntStateOf(-1)
    var isGuest by mutableStateOf(false)

    var isAvailableForCodeCheck by mutableStateOf(true)
    var isAvailableForCreatingCharacter by mutableStateOf(true)

    fun authValidation(isGuest: Boolean, userId: Int, email: String, name: String) {
        logd("AuthValidationIsGuest", "authValidation: $isGuest")
        tokenManager.saveIfGuest(isGuest)
        _registerState.value = _registerState.value.copy(
            email = email,
            repeatEmail = email,
            playerName = name,
            repeatPlayerName = name
        )
        this.isGuest = tokenPreferences.getIfGuest()
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

    fun logOutForGuest() {
        viewModelScope.launch {
            try {
                val response = MafiaApi.retrofitService.removeGuest(
                    currentPlayerId,
                    _registerState.value.playerName
                )
                logd("AuthValidationIsGuest", "logOutForGuest: $response")
                logd("AuthValidationIsGuest", "logOutForGuest: ${response.isSuccessful}")
            } catch (e: Exception) {
                loge("AuthValidationIsGuest", "logOutForGuest: ${e.message}")
            }
        }
    }
    fun registerEventHandler(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.TogglePasswordVisibility -> {
                if (_registerState.value.passwordVisualization == VisualTransformation.None) {
                    _registerState.value = _registerState.value.copy(
                        passwordVisualization = PasswordVisualTransformation(),
                        passwordIcon = Res.drawable.visibility_off
                    )
                } else {
                    _registerState.value = _registerState.value.copy(
                        passwordVisualization = VisualTransformation.None,
                        passwordIcon = Res.drawable.visibile
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
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.pleaseFillAllFields)}\n${getString(Res.string.pleaseCheckCarefullyAndTryAgain)}",
                            isTextMessageOn = true
                        )
                    }
                    return
                } else if (emailRegex.matches(_registerState.value.email).not()) {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.pleaseWriteAValidEmail)}\n${getString(Res.string.pleaseCheckCarefullyAndTryAgain)}",
                            isTextMessageOn = true
                        )
                    }
                    return
                } else if(_registerState.value.password.length < 6){
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.passwordMustContain)}\n${getString(Res.string.pleaseCheckCarefullyAndTryAgain)}",
                            isTextMessageOn = true
                        )
                    }
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
                                textMessage = "${getString(Res.string.emailDoesNotExist)}\n${getString(Res.string.pleaseCheckAndTryAgain)}!",
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
                            tokenManager.saveIfGuest(false)
                            isGuest = false
                            event.onLogIn()
                            event.callback(true)
                        }
                    } catch (e: Exception) {
                        logd("LogIn", "on Log In Click\n${e.message}")
                        _registerState.value = _registerState.value.copy(
                            isTextMessageOn = true,
                            textMessage = getString(Res.string.emailOrPasswordWrongPleaseTryAgain)
                        )
                        event.callback(true)
                    } finally {
                        event.callback(true)
                    }
                }
            }

            is RegisterEvent.OnRepeatPasswordIconClick -> {
                if ( _registerState.value.repeatPasswordVisualization == VisualTransformation.None ) {
                    _registerState.value = _registerState.value.copy(
                        repeatPasswordVisualization = PasswordVisualTransformation(),
                        repeatPasswordIcon = Res.drawable.visibility_off
                    )
                } else {
                    _registerState.value = _registerState.value.copy(
                        repeatPasswordVisualization = VisualTransformation.None,
                        repeatPasswordIcon = Res.drawable.visibile
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

                _registerState.value = _registerState.value.copy(isEmailConfirmButtonEnabled = false)

                try {
                    if (/*_registerState.value.email != _registerState.value.repeatEmail || */emailRegex.matches(
                            _registerState.value.email.trimIndent().trim()
                        ).not()
                    ) {
                        viewModelScope.launch {
                            _registerState.value = _registerState.value.copy(
                                textMessage = getString(Res.string.emailWrittenIncorrectly),
                                isTextMessageOn = true,
                                isEmailConfirmButtonEnabled = true
                            )
                        }
                    } else {
                        viewModelScope.launch {
                            try {
                                _registerState.value =
                                    _registerState.value.copy(isSpamWarning = true)
                                currentPlayerId = MafiaApi.retrofitService.register(
                                    request = PlayerRegisterDataRequest(email = _registerState.value.email.trimIndent().trim())
                                )
                                _registerState.value = _registerState.value.copy(
                                    textMessage = getString(Res.string.checkInBoxSpam),
                                    isTextMessageOn = true
                                )
                                delay(200)
                                event.onCheck()
                            } catch (e: Exception) {
                                logd("EmailCheck", "registerEventHandler: ${e.message}")
                                _registerState.value = _registerState.value.copy(
                                    textMessage = "${getString(Res.string.somethingWentWrong)}! ${getString(Res.string.error)}: ${e.message}",
                                    isTextMessageOn = true
                                )
                            } finally {
                                _registerState.value = _registerState.value.copy(isEmailConfirmButtonEnabled = true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    loge("EmailCheck", "registerEventHandler: ${e.message}")
                    _registerState.value = _registerState.value.copy(isEmailConfirmButtonEnabled = true)
                }
            }

            is RegisterEvent.OnConfirmEmailByCode -> {
                if (_registerState.value.confirmCode.trim().length != 6) {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = getString(Res.string.codeMustContain),
                            isTextMessageOn = true,
                            confirmCode = ""
                        )
                    }
                    return
                }

                this.isAvailableForCodeCheck = false
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
                        logd("Register", "registerEventHandler: ${e.message}")
                        _registerState.value = _registerState.value.copy(
                            textMessage = getString(Res.string.somethingWentWrong),
                            isTextMessageOn = true
                        )
                    } finally {
                        isAvailableForCodeCheck = true
                    }
                }
            }

            is RegisterEvent.UpdateRepeatPlayerName -> {
                _registerState.value = _registerState.value.copy(repeatPlayerName = event.name)
            }

            is RegisterEvent.OnConfirmCreateNewCharacter -> {
                _registerState.value = _registerState.value.copy(isEmailConfirmButtonEnabled = false)

                if( _registerState.value.playerName.count { it.isLetter() } < 2 ) {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.nicknameMustContain)}, ${getString(Res.string.pleaseCheckCarefullyAndTryAgain)}",
                            isTextMessageOn = true,
                            isEmailConfirmButtonEnabled = true
                        )
                    }
                }
                else if( _registerState.value.password.length < 6 ) {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.passwordMustContain)}, ${getString(Res.string.pleaseCheckCarefullyAndTryAgain)}",
                            isTextMessageOn = true,
                            isEmailConfirmButtonEnabled = true
                        )
                    }
                } else if ( _registerState.value.playerName.length < 2 || _registerState.value.password != _registerState.value.repeatPassword ) {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = "${getString(Res.string.nicknameOrPasswordAreNotUnderTerm)}\n${getString(Res.string.pleaseCheckAndTryAgain)}",
                            isTextMessageOn = true,
                            isEmailConfirmButtonEnabled = true
                        )
                    }
                } else if(/*_registerState.value.playerName == _registerState.value.repeatPlayerName &&*/ _registerState.value.password == _registerState.value.repeatPassword ) {
                    viewModelScope.launch {
                        try {
                            waitingRoomViewModel.currentPlayer = _registerState.value.playerName

                            MafiaApi.retrofitService.createCharacter(
                                key = _registerState.value.keyToCreateCharacter,
                                email = _registerState.value.email,
                                password = _registerState.value.password,
                                name = _registerState.value.playerName
                            )
                            _registerState.value = _registerState.value.copy(
                                email = "",
                                repeatEmail = "",
                                password = "",
                                repeatPassword = "",
                            )
                            tokenManager.saveIfGuest(false)
                            tokenManager.saveTokens("", "")
                            event.onConfirmCreateNewCharacter()
                        } catch (e: Exception) {
                            loge("ConfirmEmail", "registerEventHandler: ${e.message}")
                            _registerState.value = _registerState.value.copy(
                                textMessage = "Error: ${e.message}",
                                isTextMessageOn = true,
                                isEmailConfirmButtonEnabled = true
                            )
                            _registerState.value =
                                _registerState.value.copy(isEmailConfirmButtonEnabled = true)
                        } finally {
                            _registerState.value =
                                _registerState.value.copy(isEmailConfirmButtonEnabled = true)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        _registerState.value = _registerState.value.copy(
                            textMessage = getString(Res.string.somethingWentWrong),
                            isTextMessageOn = true,
                            isEmailConfirmButtonEnabled = true
                        )
                    }
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
                        sendCodeCount = 35,
                        sendCodeMessage = getString(Res.string.sendCodeAgain)
                    )
                }
            }
            is RegisterEvent.OnSpamWarningDismiss -> {
                _registerState.value = _registerState.value.copy(isSpamWarning = false)
            }
        }
    }
    fun callForGuestDialog() {
        viewModelScope.launch {
            _registerState.value = _registerState.value.copy(
                textMessage = "${getString(Res.string.guestsCannotViewAchievements)}.\n${getString(Res.string.pleaseRegisterIfYouWantToSeeAchievements)}!",
                isTextMessageOn = true
            )
        }
    }
}
