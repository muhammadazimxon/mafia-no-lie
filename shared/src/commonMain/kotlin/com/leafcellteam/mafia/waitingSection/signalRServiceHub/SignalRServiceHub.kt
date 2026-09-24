package com.leafcellteam.mafia.waitingSection.signalRServiceHub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import com.leafcellteam.mafia.logw
import com.leafcellteam.mafia.network.HubState
import com.leafcellteam.mafia.network.SignalRHubBuilder
import com.leafcellteam.mafia.network.SignalRHubConnection
import com.leafcellteam.mafia.network.on
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatMessageData
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatModel
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.SystemMessage
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.UserColor
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val HUB = "waitingRoomHub"
private const val SERVER_URL = "$DOMAIN$HUB"

class SignalRServiceHub(private val gameRoomServiceHub: GameRoomServiceHub) {
    private val hubConnection: SignalRHubConnection = SignalRHubBuilder(SERVER_URL).build()

    private val _waitingRoomDto = MutableStateFlow<WaitingRoomDto?>(null)
    val waitingRoomDto: StateFlow<WaitingRoomDto?> = _waitingRoomDto.asStateFlow()

    private val _playerId = MutableStateFlow(0)
    val playerId: StateFlow<Int> = _playerId.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessageData>?>(null)
    val messages: StateFlow<List<ChatMessageData>?> = _messages.asStateFlow()

    private val _systemMessages = MutableStateFlow(emptyList<SystemMessage>())
    val systemMessages: StateFlow<List<SystemMessage>> = _systemMessages.asStateFlow()

    private val _userColors = MutableStateFlow(emptyList<UserColor>())
    var userColors: StateFlow<List<UserColor>> = _userColors.asStateFlow()

    private val _counter = MutableStateFlow(25)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private var _isDataLoaded by mutableStateOf(false)

    private fun receiveWaitingRoomData() {
        logd("WaitingRoomViewModel", "Setting up ReceiveWaitingRoomData handler")
        hubConnection.on("ReceiveWaitingRoomData") { roomId: String, roomName: String, players: List<String>, chosenRoles: List<String>, minPlayers: Int, maxPlayers: Int ->
            logd("WaitingRoomViewModel", "Received room data: $roomId, $roomName, players: ${players.size}")
            _waitingRoomDto.value = WaitingRoomDto(roomId, roomName, players, minPlayers, maxPlayers, chosenRoles)
            gameRoomServiceHub.setGuid(roomId)
            _isDataLoaded = true
        }
    }

    private fun setupReceivePlayerId() {
        hubConnection.remove("ReceivePlayerId")
        hubConnection.on("ReceivePlayerId") { receivedId: Int ->
            _playerId.value = receivedId
        }
    }

    private fun setupReceivePlayers() {
        hubConnection.remove("ReceivePlayers")
        hubConnection.on("ReceivePlayers") { players: List<String> ->
            _waitingRoomDto.value = _waitingRoomDto.value?.copy(players = players) ?: _waitingRoomDto.value
        }
    }

    private fun setupReceiveMessage() {
        hubConnection.remove("ReceiveMessage")
        hubConnection.on("ReceiveMessage") { chatModels: List<ChatModel?> ->
            val updatedChatModels = chatModels.map { ChatMessageData(it?.playerId ?: 0, it?.playerName ?: "Loading...", it?.message ?: "") }
            _messages.value = updatedChatModels
        }
    }

    private fun clearMessages() {
        _messages.value = null
    }

    private fun setupBackgroundImage() {
        hubConnection.remove("GetBackgroundImage")
        hubConnection.on("GetBackgroundImage") { playerNames: List<String>, backgroundImages: List<List<Int>> ->
            val backgroundColors = backgroundImages
            this._userColors.value = playerNames.zip(backgroundColors).map { (name, color) ->
                UserColor(name, Color(color[0], color[1], color[2]))
            }
        }
    }

    private fun setupSystemMessage() {
        hubConnection.remove("SystemMessage")
        hubConnection.on("SystemMessage") { content: String ->
            val newSystemMessage = SystemMessage(content)
            _systemMessages.value += newSystemMessage
        }
    }

    private fun setupStartGame() {
        hubConnection.remove("GoToGame")
        hubConnection.on("GoToGame") { _: Unit ->
            logd("SignalR", "GoToGame")
            hubConnection.stop()
            gameRoomServiceHub.startConnection()
        }
    }

    private fun setupTimer() {
        hubConnection.remove("GetTimer")
        hubConnection.on("GetTimer") { counter: Int ->
            _counter.value = counter
        }
    }

    fun resetWaitingRoomData() {
        this._waitingRoomDto.value = null
        this._messages.value = emptyList()
        this._userColors.value = emptyList()
        this._isDataLoaded = false
    }

    fun sendPlayerMessage(guid: String, playerId: Int, playerName: String, message: String) {
        try {
            if (playerName.isNotBlank() && message.isNotBlank()) {
                hubConnection.send("SendMessage", guid, playerId, playerName, message)
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "sendPlayerMessage: ${e.message}")
        }
    }

    fun startConnection() {
        logd("WaitingRoomViewModel", "Attempting to connect to hub")
        try {
            if(hubConnection.state == HubState.DISCONNECTED) {
                logd("WaitingRoomViewModel", "Starting hub connection")
                hubConnection.start()
            } else {
                logd("WaitingRoomViewModel", "Hub already connected: ${hubConnection.state}")
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "Exception in waitingRoomConnection: ${e.message}")
        }
    }

    fun sendLeaveRoom(guid: String, playerId: Int) {
        try {
            hubConnection.send("LeaveRoom", guid, playerId)
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "sendLeaveRoom: ${e.message}")
        }
    }

    fun requestJoinGame(guid: String, playerName: String, playerId: Int) {
        clearMessages()
        try {
            if (guid.isBlank()) {
                loge("HUB", "Invalid GUID: Empty string")
                return
            }
            if (hubConnection.state == HubState.CONNECTED) {
                hubConnection.send("JoinGame", guid, playerName, playerId)
                logd("HUB", "JoinGame request sent")
                setupReceiveMessage()
            } else {
                logw("SignalR", "Error: hub not connected")
            }
        } catch (e: Exception) {
            loge("SignalR", "Send error JoinGame: ${e.message}")
        }
    }

    fun requestStartGame(guid: String) {
        try {
            if(guid.isNotBlank()) {
                hubConnection.send("StartGame", guid)
            } else {
                logw("SignalR", "Cannot requestStartGame now (guid='$guid')")
            }
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "requestStartGame: ${e.message}")
        }
    }

    fun changePlayerID(playerId: Int) {
        _playerId.value = playerId
    }

    fun stopConnection() {
        try {
            hubConnection.stop()
        } catch (e: Exception) {
            loge("WaitingRoomViewModel", "stopConnection: ${e.message}")
        }
    }

    fun reset() {
        _waitingRoomDto.value = _waitingRoomDto.value?.copy(
            roomName = "",
            players = emptyList(),
            maxPlayers = 0,
            chosenRoles = emptyList()
        )
        _isDataLoaded = false
        _messages.value = null
        _userColors.value = emptyList()
        _systemMessages.value = emptyList()
    }

    fun resetCounter() {
        _counter.value = 25
    }

    fun setupHub() {
        receiveWaitingRoomData()
        setupReceivePlayerId()
        setupBackgroundImage()
        setupReceivePlayers()
        setupReceiveMessage()
        setupSystemMessage()
        setupStartGame()
        setupTimer()
    }

    fun getHubConnection(): SignalRHubConnection = hubConnection
}
