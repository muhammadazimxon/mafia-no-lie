package com.example.mafiaonlinejetpackcomposecapi.creatingSection.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Phase
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameViewModel.GameRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.CreateRoomRequest
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateGameViewModel(private val waitingRoomViewModel: WaitingRoomViewModel, private val gameRoomViewModel: GameRoomViewModel) : ViewModel() {
    var createGameState by mutableStateOf(CreateGameState())

    fun createGameEventHandler(event: CreateGameEvent) {
        when(event) {
            is CreateGameEvent.TogglePasswordVisibility -> {
                createGameState = createGameState.copy(isShowPasswordState = event.isShowPasswordState)
            }

            is CreateGameEvent.OnChoseCivilianRole -> {
                createGameState = createGameState.copy(
                    civilianRoles = createGameState.civilianRoles
                        .mapIndexed { index, roleData ->
                            if(index == event.clickedPosition) roleData.copy(isChosen = event.isChosen)
                            else roleData
                        }
                )
            }

            is CreateGameEvent.OnChoseMafiaRole -> {
                createGameState = createGameState.copy(
                    mafiaRoles = createGameState.mafiaRoles
                        .mapIndexed { index, roleData ->
                            if(index == event.clickedPosition) roleData.copy(isChosen = event.isChosen)
                            else roleData
                        }
                )
            }

            is CreateGameEvent.UpdatePassword -> {
                createGameState = createGameState.copy(password = event.password)
            }

            is CreateGameEvent.UpdateRangeOfPlayers -> {
                if(event.rangeOfPlayers.start.toInt() + 1 != event.rangeOfPlayers.endInclusive.toInt() && event.rangeOfPlayers.start.toInt() != event.rangeOfPlayers.endInclusive.toInt()) {
                    createGameState = createGameState.copy(rangeOfPlayers = event.rangeOfPlayers)
                }
            }

            is CreateGameEvent.UpdateRoomName -> {
                createGameState = createGameState.copy(isErrorRoomName = (event.roomName.isBlank() || event.roomName.isEmpty() || event.roomName.length < 3))
                createGameState = createGameState.copy(roomName = event.roomName)
            }

            is CreateGameEvent.CivilianHelpButton -> {
                createGameState = createGameState.copy(helpButtonState = createGameState.civilianRoles[event.clickedPosition])
            }

            is CreateGameEvent.MafiaHelpButton -> {
                createGameState = createGameState.copy(helpButtonState = createGameState.mafiaRoles[event.clickedPosition])
            }

            is CreateGameEvent.DismissHelpButton -> {
                createGameState = createGameState.copy(helpButtonState = null)
            }

            is CreateGameEvent.CreateGameAction -> {
                viewModelScope.launch {
                    try {
                        if(createGameState.isNotCreated) {
                            createGameState = createGameState.copy(isNotCreated = false)
                            MafiaApi.retrofitService.createRoom(
                                CreateRoomRequest(
                                    roomName = createGameState.roomName,
                                    playerName = waitingRoomViewModel.currentPlayer,
                                    minPlayers = createGameState.rangeOfPlayers.start.toInt(),
                                    maxPlayers = createGameState.rangeOfPlayers.endInclusive.toInt(),
                                    allowedRoles = (createGameState.civilianRoles + createGameState.mafiaRoles).filter { it.isChosen }
                                        .map { it.role::class.simpleName.toString().uppercase() },
                                    password = createGameState.password,
                                    phase = if (createGameState.isDayPhase) Phase.DayDiscussion.name else Phase.NightDiscussion.name
                                )
                            )
                            waitingRoomViewModel.guid = MafiaApi.retrofitService.getGuid()
                            delay(300)
                            waitingRoomViewModel.startConnection()
                            createGameState = createGameState.copy(isLoading = true)
                            delay(1500)
                            if (waitingRoomViewModel.getHubConnection().connectionState == HubConnectionState.CONNECTED) {
                                event.onCreate()
                                createGameState = createGameState.copy(isNotCreated = true)
                                createGameState = createGameState.copy(isLoading = false)
                            }
                            gameRoomViewModel.changePhase(isDay = createGameState.isDayPhase)
                            delay(300)
                            gameRoomViewModel.sendPhase()
                        }
                    } catch(e: Exception) {
                        Log.d("CREATE_VIEW_MODEL", "createGameEventHandler: ${e.message}")
                    }
                }
            }

            is CreateGameEvent.ChangePhase -> {
                createGameState = createGameState.copy(isDayPhase = event.isDay, isNightPhase = event.isNight)
            }
        }
    }
    fun handleEventWithResult(event: CreateGameEventWithResult): Boolean {
        return when (event) {
            is CreateGameEventWithResult.IsRoleBalanceOK -> {
                val param = event.rolesChooserParam
                (createGameState.civilianRoles.sumOf { if( it.isChosen ) 1 as Int else 0 } == createGameState.mafiaRoles.sumOf{ if( it.isChosen ) 1 as Int else 0 } ||
                        createGameState.rangeOfPlayers.start >= 11 ||
                        (param.role.category == Role.Category.CIVILIAN && createGameState.civilianRoles.sumOf { if( it.isChosen ) 1 as Int else 0 } + 1 == createGameState.mafiaRoles.sumOf { if( it.isChosen ) 1 as Int else 0 }) ||
                        (param.role.category == Role.Category.MAFIA && createGameState.civilianRoles.sumOf { if( it.isChosen ) 1 as Int else 0 } == createGameState.mafiaRoles.sumOf { if( it.isChosen ) 1 as Int else 0 } + 1)
                        ) || event.rolesChooserParam.value
            }
        }
    }
}