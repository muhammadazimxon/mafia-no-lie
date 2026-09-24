//package com.leafcellteam.mafia.waitingSection.waitingRoomViewModel
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
//import com.leafcellteam.mafia.waitingSection.mvi.WaitingRoomEvent
//import com.leafcellteam.mafia.waitingSection.mvi.WaitingRoomState
//import com.leafcellteam.mafia.waitingSection.signalRServiceHub.SignalRServiceHub
//import com.leafcellteam.mafia.waitingSection.waitingRoomModels.UserColor
//import com.leafcellteam.mafia.network.HubState
//import com.leafcellteam.mafia.logd
//import com.leafcellteam.mafia.loge
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class WaitingRoomViewModel(private val signalRServiceHub: SignalRServiceHub, private val gameRoomViewModel: GameRoomViewModel) : ViewModel() {
//    private val _uiState = MutableStateFlow(WaitingRoomState())
//    val uiState: StateFlow<WaitingRoomState> = _uiState.asStateFlow()
//
//    private var joined by mutableStateOf(false)
//    var guid by mutableStateOf("")
//
//    var messageState by mutableStateOf("")
//    val gameCounter = signalRServiceHub.counter
//    var currentPlayer by mutableStateOf("")
//
//    private val _playerId: StateFlow<Int> = signalRServiceHub.playerId
//    val userColors: StateFlow<List<UserColor>> = signalRServiceHub.userColors
//
//    init {
//        startConnection()
//        signalRServiceHub.setupHub()
//
//        viewModelScope.launch {
//            signalRServiceHub.waitingRoomDto.collect { dto ->
//                _uiState.update { it.copy(roomDto = dto) }
//            }
//        }
//
//        viewModelScope.launch {
//            signalRServiceHub.messages.collect { msgs ->
//                _uiState.update { it.copy(messages = msgs) }
//            }
//        }
//
//        viewModelScope.launch {
//            signalRServiceHub.systemMessages.collect { sys ->
//                _uiState.update { it.copy(systemMessage = sys) }
//            }
//        }
//
//        viewModelScope.launch {
//            signalRServiceHub.userColors.collect { cols ->
//                _uiState.update { it.copy(userColors = cols) }
//            }
//        }
//
//        viewModelScope.launch {
//            signalRServiceHub.playerId.collect { id ->
//                _uiState.update { it.copy(playerId = id) }
//            }
//        }
//    }
//
//    fun startConnection() {
//        signalRServiceHub.startConnection()
//    }
//
//    fun waitingRoomEventHandler(event: WaitingRoomEvent) {
//        when(event) {
//            is WaitingRoomEvent.UpdateMessage -> {
//                messageState = event.newMessage
//            }
//
//            is WaitingRoomEvent.SubmitMessage -> {
//                logd("WaitingRoom", "onSubmitMessage: Sending message: ${event.message}")
//                if (event.message.isNotBlank()) {
//                    sendPlayerMessage(guid, currentPlayer, event.message)
//                    messageState = ""
//                }
//            }
//        }
//    }
//
//    fun waitingRoomRequests() {
//        logd("WaitingRoom", "LaunchedEffect(connectionState): Connection state changed: ${getHubConnection().state}")
//        if (getHubConnection().state == HubState.CONNECTED && guid.isNotBlank() || !joined) {
//            logd("WaitingRoom", "LaunchedEffect(connectionState): Connected, requesting join game")
//            requestJoinGame(guid, currentPlayer)
//            joined = true
//            _uiState.update { it.copy(backHandlerEnabled = true) }
//            gameRoomViewModel.changePlayerName(currentPlayer)
//            if(_playerId.value != -1) {
//                gameRoomViewModel.changePlayerId(_playerId.value)
//            }
//        }
//    }
//
//    fun backHandlerFunc(onBack: () -> Unit) {
//        viewModelScope.launch {
//            logd("WaitingRoom", "BackHandler: Back button pressed")
//            try {
//                if (getHubConnection().state == HubState.CONNECTED &&
//                    _playerId.value != 0 && guid.isNotBlank()
//                ) {
//                    logd("WaitingRoom", "BackHandler: Sending leave room request")
//                    sendLeaveRoom(guid, _playerId.value)
//                    changeBackState(true)
//                    delay(200)
//                    if (getHubConnection().state == HubState.CONNECTED) {
//                        stopConnection()
//                    }
//                }
//            } catch (e: Exception) {
//                loge("WaitingRoom", "BackHandler: Error leaving room: ${e.message}")
//            } finally {
//                logd("WaitingRoom", "BackHandler: Stopping connection and navigating back")
//                joined = false
//                changeBackState(false)
//                onBack()
//                waitingRoomReset()
//            }
//        }
//    }
//
//    fun waitingRoomConnection() {
//        logd("WaitingRoom", "LaunchedEffect(true): Starting connection")
//        if (guid.isBlank()) {
//            loge("WaitingRoom", "LaunchedEffect(true): waitingViewModel guid is blank")
//        }
//        startConnection()
//    }
//
//    fun waitingRoomCounter(nextRoom: () -> Unit) {
//        logd("WaitingRoom", "LaunchedEffect(isStartState, gameCounter): isStartState = ${gameCounter.value}, gameCounter = ${gameCounter.value}")
//        logd("WaitingRoom", "LaunchedEffect(isStartState, gameCounter): Navigating to next room")
//        signalRServiceHub.requestStartGame(guid)
//        nextRoom()
//        resetGameCounter()
//    }
//
//    private fun changeBackState(backState: Boolean) = _uiState.update { it.copy(isBackState = backState) }
//    fun changePlayerID(playerId: Int) {
//        signalRServiceHub.changePlayerID(playerId)
//        gameRoomViewModel.changePlayerId(playerId)
//        loge("ChangePlayerId", "changePlayerID: $playerId")
//    }
//
//    fun leaveWaitingRoom() {
//        signalRServiceHub.sendLeaveRoom(guid, signalRServiceHub.playerId.value)
//    }
//
//    private fun requestJoinGame(guid: String, playerName: String) = signalRServiceHub.requestJoinGame(guid, playerName, gameRoomViewModel.currentPlayerDataState.value.playerId)
//    private fun sendLeaveRoom(guid: String, playerId: Int) = signalRServiceHub.sendLeaveRoom(guid, playerId)
//    private fun sendPlayerMessage(guid: String, playerName: String, message: String) = signalRServiceHub.sendPlayerMessage(guid, gameRoomViewModel.currentPlayerDataState.value.playerId, playerName, message)
//    private fun resetGameCounter() = signalRServiceHub.resetCounter()
//    fun setupHubHandler() = signalRServiceHub.setupHub()
//    fun stopConnection() = signalRServiceHub.stopConnection()
//    fun getHubConnection() = signalRServiceHub.getHubConnection()
//
//    private fun waitingRoomReset() {
//        _uiState.value = _uiState.value.copy(
//            roomDto = null,
//            messages = null,
//            systemMessage = null,
//            gameCounter = 25,
//            userColors = emptyList(),
//            isBackState = false,
//            backHandlerEnabled = false,
//            messageDraft = "",
//            counter = 25
//        )
//        signalRServiceHub.reset()
//    }
//
//    override fun onCleared() {
//        stopConnection()
//        super.onCleared()
//    }
//}


package com.leafcellteam.mafia.waitingSection.waitingRoomViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
import com.leafcellteam.mafia.waitingSection.mvi.WaitingRoomEvent
import com.leafcellteam.mafia.waitingSection.mvi.WaitingRoomState
import com.leafcellteam.mafia.network.SignalRServiceHub
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.UserColor
import com.leafcellteam.mafia.network.HubState
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WaitingRoomViewModel(private val signalRServiceHub: SignalRServiceHub, private val gameRoomViewModel: GameRoomViewModel) : ViewModel() {
    private val _uiState = MutableStateFlow(WaitingRoomState())
    val uiState: StateFlow<WaitingRoomState> = _uiState.asStateFlow()

    private var joined by mutableStateOf(false)
    var guid by mutableStateOf("")

    var messageState by mutableStateOf("")
    val gameCounter = signalRServiceHub.counter
    var currentPlayer by mutableStateOf("")

    private val _playerId: StateFlow<Int> = signalRServiceHub.playerId
    val userColors: StateFlow<List<UserColor>> = signalRServiceHub.userColors

    init {
        startConnection()
        signalRServiceHub.setupHub()

        viewModelScope.launch {
            signalRServiceHub.waitingRoomDto.collect { dto ->
                _uiState.update { it.copy(roomDto = dto) }
            }
        }

        viewModelScope.launch {
            signalRServiceHub.messages.collect { msgs ->
                _uiState.update { it.copy(messages = msgs) }
            }
        }

        viewModelScope.launch {
            signalRServiceHub.systemMessages.collect { sys ->
                _uiState.update { it.copy(systemMessage = sys) }
            }
        }

        viewModelScope.launch {
            signalRServiceHub.userColors.collect { cols ->
                _uiState.update { it.copy(userColors = cols) }
            }
        }

        viewModelScope.launch {
            signalRServiceHub.playerId.collect { id ->
                _uiState.update { it.copy(playerId = id) }
            }
        }
    }

    fun startConnection() {
        signalRServiceHub.startConnection()
    }

    fun waitingRoomEventHandler(event: WaitingRoomEvent) {
        when(event) {
            is WaitingRoomEvent.UpdateMessage -> {
                messageState = event.newMessage
            }

            is WaitingRoomEvent.SubmitMessage -> {
                logd("WaitingRoom", "onSubmitMessage: Sending message: ${event.message}")
                if (event.message.isNotBlank()) {
                    sendPlayerMessage(guid, currentPlayer, event.message)
                    messageState = ""
                }
            }
        }
    }

    fun waitingRoomRequests() {
        logd("WaitingRoom", "LaunchedEffect(connectionState): Connection state changed: ${getHubConnection().state}")
        if (getHubConnection().state == HubState.CONNECTED && guid.isNotBlank() || !joined) {
            logd("WaitingRoom", "LaunchedEffect(connectionState): Connected, requesting join game")
            requestJoinGame(guid, currentPlayer)
            joined = true
            _uiState.update { it.copy(backHandlerEnabled = true) }
            gameRoomViewModel.changePlayerName(currentPlayer)
            if(_playerId.value != -1) {
                gameRoomViewModel.changePlayerId(_playerId.value)
            }
        }
    }

    fun backHandlerFunc(onBack: () -> Unit) {
        viewModelScope.launch {
            logd("WaitingRoom", "BackHandler: Back button pressed")
            try {
                if (getHubConnection().state == HubState.CONNECTED &&
                    _playerId.value != 0 && guid.isNotBlank()
                ) {
                    logd("WaitingRoom", "BackHandler: Sending leave room request")
                    sendLeaveRoom(guid, _playerId.value)
                    changeBackState(true)
                    delay(200)
                    if (getHubConnection().state == HubState.CONNECTED) {
                        stopConnection()
                    }
                }
            } catch (e: Exception) {
                loge("WaitingRoom", "BackHandler: Error leaving room: ${e.message}")
            } finally {
                logd("WaitingRoom", "BackHandler: Stopping connection and navigating back")
                joined = false
                changeBackState(false)
                onBack()
                waitingRoomReset()
            }
        }
    }

    fun waitingRoomConnection() {
        logd("WaitingRoom", "LaunchedEffect(true): Starting connection")
        if (guid.isBlank()) {
            loge("WaitingRoom", "LaunchedEffect(true): waitingViewModel guid is blank")
        }
        startConnection()
    }

    fun waitingRoomCounter(nextRoom: () -> Unit) {
        logd("WaitingRoom", "LaunchedEffect(isStartState, gameCounter): isStartState = ${gameCounter.value}, gameCounter = ${gameCounter.value}")
        logd("WaitingRoom", "LaunchedEffect(isStartState, gameCounter): Navigating to next room")
        signalRServiceHub.requestStartGame(guid)
        nextRoom()
        resetGameCounter()
    }

    private fun changeBackState(backState: Boolean) = _uiState.update { it.copy(isBackState = backState) }
    fun changePlayerID(playerId: Int) {
        signalRServiceHub.changePlayerID(playerId)
        gameRoomViewModel.changePlayerId(playerId)
        loge("ChangePlayerId", "changePlayerID: $playerId")
    }

    fun leaveWaitingRoom() {
        signalRServiceHub.sendLeaveRoom(guid, signalRServiceHub.playerId.value)
    }

    private fun requestJoinGame(guid: String, playerName: String) = signalRServiceHub.requestJoinGame(guid, playerName, gameRoomViewModel.currentPlayerDataState.value.playerId)
    private fun sendLeaveRoom(guid: String, playerId: Int) = signalRServiceHub.sendLeaveRoom(guid, playerId)
    private fun sendPlayerMessage(guid: String, playerName: String, message: String) = signalRServiceHub.sendPlayerMessage(guid, gameRoomViewModel.currentPlayerDataState.value.playerId, playerName, message)
    private fun resetGameCounter() = signalRServiceHub.resetCounter()
    fun setupHubHandler() = signalRServiceHub.setupHub()
    fun stopConnection() = signalRServiceHub.stopConnection()
    fun getHubConnection() = signalRServiceHub.getHubConnection()

    private fun waitingRoomReset() {
        _uiState.value = _uiState.value.copy(
            roomDto = null,
            messages = null,
            systemMessage = null,
            gameCounter = 25,
            userColors = emptyList(),
            isBackState = false,
            backHandlerEnabled = false,
            messageDraft = "",
            counter = 25
        )
        signalRServiceHub.reset()
    }

    override fun onCleared() {
        stopConnection()
        super.onCleared()
    }
}
