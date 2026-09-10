package com.leafcellteam.mafia.joinRoom.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.shared.joinRoom.RoomData
import com.leafcellteam.shared.joinRoom.RoomEvent
import com.leafcellteam.shared.joinRoom.RoomState
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomsScreenViewModel(private val waitingRoomViewModel: WaitingRoomViewModel) : ViewModel() {
    private val _state = MutableStateFlow<RoomState>(RoomState())
    val state: StateFlow<RoomState> = _state.asStateFlow()

    fun onEventHandler(event: RoomEvent) {
        when(event) {
            is RoomEvent.OnJoin -> {
                try {
                    val room = _state.value.roomsList.getOrNull(event.index)
                    if (room?.isClickedAlready == true) {
                        Log.d("JOIN", "Room already clicked, ignoring")
                        return
                    }

                    _state.value = _state.value.copy(
                        roomsList = _state.value.roomsList.mapIndexed { idx, it ->
                            if(event.index == idx) it.copy(isClickedAlready = true) else it
                        }
                    )

                    waitingRoomViewModel.guid = event.guid

                    if (event.guid.isBlank()) {
                        Log.e("JOIN", "GUID is blank!")
                        _state.value = _state.value.copy(
                            roomsList = _state.value.roomsList.mapIndexed { idx, it ->
                                if(event.index == idx) it.copy(isClickedAlready = false) else it
                            }
                        )
                    } else {
                        waitingRoomViewModel.startConnection()

                        viewModelScope.launch {
                            var attempts = 0
                            val maxAttempts = 30
                            var connected = false

                            while (attempts < maxAttempts && !connected) {
                                delay(500)

                                val connectionState = waitingRoomViewModel.getHubConnection().connectionState
                                Log.d("JOIN", "Connection attempt ${attempts + 1}: $connectionState")

                                when (connectionState) {
                                    HubConnectionState.CONNECTED -> {
                                        connected = true
                                        event.onJoin()
                                        _state.value = _state.value.copy(isLoading = false)
                                    }
                                    HubConnectionState.DISCONNECTED -> {
                                        if (attempts < maxAttempts - 1) {
                                            Log.d("JOIN", "Reconnecting...")
                                            waitingRoomViewModel.startConnection()
                                        }
                                    }
                                    else -> {
                                        _state.value = _state.value.copy(isLoading = true)
                                    }
                                }

                                attempts++
                            }

                            if (!connected) {
                                Log.e("JOIN", "Failed to connect after $maxAttempts attempts")
                                _state.value = _state.value.copy(
                                    roomsList = _state.value.roomsList.mapIndexed { idx, it ->
                                        if(event.index == idx) it.copy(isClickedAlready = false) else it
                                    },
                                    errorMessage = "Failed to connect to room after $maxAttempts attempts"
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("JOIN", "Error joining room: ${e.message}")
                    _state.value = _state.value.copy(
                        roomsList = _state.value.roomsList.mapIndexed { idx, it ->
                            if(event.index == idx) it.copy(isClickedAlready = false) else it
                        },
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }

            is RoomEvent.OnPasswordDialogOpen -> {
                _state.value = _state.value.copy(
                    dialogsGuid = event.guid,
                    correctPassword = event.correctPass,
                    isPasswordDialogOn = true,
                    password = "",
                    passwordError = false,
                    joinViaPasswordIndex = event.index
                )
            }

            is RoomEvent.OnRandom -> {
                val availableRooms = _state.value.roomsList
                    .mapIndexed { index, room -> IndexedValue(index, room) }
                    .filter { it.value.playerQuantity > 0 && !it.value.isClickedAlready }

                if (availableRooms.isEmpty()) {
                    _state.value = _state.value.copy(errorMessage = "No available rooms")
                    return
                }

                val openRooms = availableRooms.filter { it.value.password.isBlank() }
                val selectedRoom = if (openRooms.isNotEmpty()) {
                    openRooms.random()
                } else {
                    availableRooms.random()
                }

                val roomIndex = selectedRoom.index
                val room = selectedRoom.value

                if (room.password.isBlank()) {
                    onEventHandler(RoomEvent.OnJoin(room.roomId, event.onJoin, roomIndex))
                } else {
                    onEventHandler(RoomEvent.OnPasswordDialogOpen(room.password, room.roomId, roomIndex))
                }
            }

            is RoomEvent.OnUpdate -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                    try {
                        val rooms = MafiaApi.retrofitService.update()
                        _state.value = _state.value.copy(roomsList = rooms)
                    } catch (e: Exception) {
                        Log.e("UPDATE", "Failed to update rooms: ${e.message}")
                        _state.value = _state.value.copy(
                            errorMessage = "Failed to update rooms: ${e.message}"
                        )
                    } finally {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }

            is RoomEvent.Dismiss -> {
                _state.value = _state.value.copy(
                    isPasswordDialogOn = false,
                    password = "",
                    passwordError = false
                )
            }

            is RoomEvent.JoinWithPassword -> {
                if (_state.value.password == _state.value.correctPassword) {
                    val room = _state.value.roomsList.getOrNull(event.joinViaPasswordIndex)
                    if (room?.isClickedAlready == true) {
                        Log.d("JOIN", "Room already clicked via password, ignoring")
                        _state.value = _state.value.copy(isPasswordDialogOn = false)
                        return
                    }

                    _state.value = _state.value.copy(
                        isPasswordDialogOn = false,
                        password = "",
                        passwordError = false
                    )
                    onEventHandler(RoomEvent.OnJoin(
                        _state.value.dialogsGuid,
                        event.onJoin,
                        event.joinViaPasswordIndex
                    ))
                } else {
                    _state.value = _state.value.copy(passwordError = true)
                }
            }

            is RoomEvent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(showPassword = !_state.value.showPassword)
            }

            is RoomEvent.UpdatePassword -> {
                _state.value = _state.value.copy(
                    password = event.newPassword,
                    passwordError = false
                )
            }
        }
    }

    fun resetRooms() {
        _state.value = _state.value.copy(
            roomsList = emptyList(),
            errorMessage = null,
            isPasswordDialogOn = false
        )
    }
}