package com.example.mafiaonlinejetpackcomposecapi.joinRoom.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
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
                    waitingRoomViewModel.guid = event.guid
                    if ( event.guid.isBlank() ) {
                        Log.e("JOIN", "GUID is blank!")
                    } else {
                        waitingRoomViewModel.startConnection()
                        viewModelScope.launch {
                            delay(1000)
                            if (waitingRoomViewModel.getHubConnection().connectionState == HubConnectionState.CONNECTED) {
                                event.onJoin()
                            } else {
                                Log.d("ROOM SCREEN", "RoomScreen: Error")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("JOIN", "Error joining room: ${e.message}")
                }
            }

            is RoomEvent.OnPasswordDialogOpen -> {
                _state.value = _state.value.copy(dialogsGuid = event.guid)
                _state.value = _state.value.copy(correctPassword = event.correctPass)
                _state.value = _state.value.copy(isPasswordDialogOn = true)
                _state.value = _state.value.copy(password = "")
                _state.value = _state.value.copy(passwordError = false)
            }

            is RoomEvent.OnRandom -> {
                val openedRooms = _state.value.roomsList.filter { it.password.isBlank() }
                val room = if (openedRooms.isNotEmpty()) openedRooms.random() else _state.value.roomsList.randomOrNull()
                room?.let {
                    if (it.password.isBlank()) {
                        onEventHandler(RoomEvent.OnJoin(it.roomId, event.onJoin))
                    } else {
                        onEventHandler(RoomEvent.OnPasswordDialogOpen(it.password, it.roomId))
                    }
                } ?: run { _state.value = _state.value.copy(errorMessage = "No rooms available") }
            }

            is RoomEvent.OnUpdate -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    try {
                        _state.value = _state.value.copy(roomsList = MafiaApi.retrofitService.update())
                        _state.value = _state.value.copy(errorMessage = null)
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(errorMessage = "Failed to update rooms: ${e.message}")
                    } finally {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }

            is RoomEvent.Dismiss -> {
                _state.value = _state.value.copy(isPasswordDialogOn = false)
            }

            is RoomEvent.JoinWithPassword -> {
                if (_state.value.password == _state.value.correctPassword) {
                    onEventHandler(RoomEvent.OnJoin(_state.value.dialogsGuid, event.onJoin))
                    _state.value = _state.value.copy(isPasswordDialogOn = false)
                } else {
                    _state.value = _state.value.copy(passwordError = true)
                }
            }

            is RoomEvent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(showPassword = !_state.value.showPassword)
            }

            is RoomEvent.UpdatePassword -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
        }
    }

    fun resetRooms() {
        _state.value = _state.value.copy(roomsList = emptyList())
    }
}