package com.leafcellteam.mafia.creatingSection.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
import com.leafcellteam.shared.gameRoom.models.Phase
import com.leafcellteam.shared.network.models.CreateRoomRequest
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.shared.roles.Role
import com.leafcellteam.shared.roles.RoleData
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

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
                        if (createGameState.isNotCreated) {
                            createGameState = createGameState.copy(
                                isNotCreated = false,
                                isLoading = true
                            )

                            Log.d("CREATE", "Creating room...")

                            MafiaApi.retrofitService.createRoom(
                                CreateRoomRequest(
                                    roomName = createGameState.roomName,
                                    playerName = waitingRoomViewModel.currentPlayer,
                                    minPlayers = createGameState.rangeOfPlayers.start.toInt(),
                                    maxPlayers = createGameState.rangeOfPlayers.endInclusive.toInt(),
                                    country = event.country,
                                    allowedRoles = (createGameState.civilianRoles + createGameState.mafiaRoles)
                                        .filter { it.isChosen }
                                        .map { it.role::class.simpleName.toString().uppercase() },
                                    password = createGameState.password,
                                    phase = if (createGameState.isDayPhase)
                                        Phase.DayDiscussion.name
                                    else
                                        Phase.NightDiscussion.name
                                )
                            )
                            Log.d("CREATE", "Room created successfully")

                            waitingRoomViewModel.guid = MafiaApi.retrofitService.getGuid()
                            Log.d("CREATE", "Got guid=${waitingRoomViewModel.guid}")

                            waitingRoomViewModel.startConnection()
                            Log.d("CREATE", "Connecting to hub...")

                            val connected = withTimeoutOrNull(10_000) {
                                while (isActive) {
                                    val state = waitingRoomViewModel.getHubConnection().connectionState
                                    Log.d("CREATE", "connectionState=$state")
                                    if (state == HubConnectionState.CONNECTED) {
                                        Log.d("CREATE", "Connected successfully!")
                                        return@withTimeoutOrNull true
                                    }
                                    delay(200)
                                }
                                false
                            } ?: false

                            if (connected) {
                                event.onCreate()
                                gameRoomViewModel.changePhase(isDay = createGameState.isDayPhase)
                                gameRoomViewModel.sendPhase()
                            } else {
                                Log.e("CREATE", "Connection timeout")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CREATE_VIEW_MODEL", "createGameEventHandler: ${e.message}", e)
                    } finally {
                        createGameState = createGameState.copy(
                            isLoading = false,
                            isNotCreated = true
                        )
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

    fun resetState() {
        createGameState = createGameState.copy(
            civilianRoles = listOf(
                RoleData(Role.Sherif),
                RoleData(Role.Detective),
                RoleData(Role.Doctor),
                RoleData(Role.Lover),
                RoleData(Role.Mimic),
                RoleData(Role.Journalist)
            ),
            mafiaRoles = listOf(
                RoleData(Role.Barman),
                RoleData(Role.Informator),
                RoleData(Role.Bomber),
                RoleData(Role.Don)
            ),
            rangeOfPlayers = 5f..15f,
            roomName = "",
            isErrorRoomName = true
        )
    }
}