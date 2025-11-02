package com.example.mafiaonlinejetpackcomposecapi.waitingSection.signalRServiceHub

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.DOMAIN
import com.example.mafiaonlinejetpackcomposecapi.HOST_1
import com.example.mafiaonlinejetpackcomposecapi.PORT_2
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.ChatMessageData
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.ChatModel
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.SystemMessage
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.UserColor
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.WaitingRoomDto
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val HUB = "waitingRoomHub"

private const val SERVER_URL = "$DOMAIN$HUB"

class SignalRServiceHub(private val gameRoomServiceHub: GameRoomServiceHub) {
    private val hubConnection: HubConnection = HubConnectionBuilder
        .create(SERVER_URL)
        .withHandshakeResponseTimeout(30000)
        .build()

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

    private val _counter = MutableStateFlow(35)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private var _isDataLoaded by mutableStateOf(false)

    private fun receiveWaitingRoomData() {
        Log.d("WaitingRoomViewModel", "Setting up ReceiveWaitingRoomData handler")
        hubConnection.on("ReceiveWaitingRoomData", {
                roomId: String,
                roomName: String,
                players: Array<String>,
                chosenRoles: Array<String>,
                minPlayers: Int,
                maxPlayers: Int ->
            Log.d("WaitingRoomViewModel", "Received room data: $roomId, $roomName, players: ${players.size}")
            _waitingRoomDto.value = WaitingRoomDto(roomId, roomName, players.toList(), minPlayers, maxPlayers, chosenRoles.toList())
            gameRoomServiceHub.setGuid(roomId)
            _isDataLoaded = true
        },
            String::class.java,
            String::class.java,
            Array<String>::class.java,
            Array<String>::class.java,
            Int::class.java,
            Int::class.java
        )
    }

    private fun setupReceivePlayerId() {
        hubConnection.remove("ReceivePlayerId")
        hubConnection.on("ReceivePlayerId", { receivedId: Int ->
            _playerId.value = receivedId
        }, Int::class.java)
    }

    private fun setupReceivePlayers() {
        hubConnection.remove("ReceivePlayers")
        hubConnection.on("ReceivePlayers", { players: Array<String> ->
            _waitingRoomDto.value = _waitingRoomDto.value?.copy(players = players.toList()) ?: _waitingRoomDto.value
        }, Array<String>::class.java)
    }

    private fun setupReceiveMessage() {
        hubConnection.remove("ReceiveMessage")
        hubConnection.on("ReceiveMessage", { chatModels: Array<ChatModel?> ->
            val updatedChatModels = chatModels.map { ChatMessageData(it?.playerId ?: 0, it?.playerName ?: "Loading...", it?.message ?: "") }
            _messages.value = updatedChatModels
        }, Array<ChatModel>::class.java)
    }

    private fun clearMessages() {
        _messages.value = null
    }

    private fun setupBackgroundImage() {
        hubConnection.remove("GetBackgroundImage")
        hubConnection.on("GetBackgroundImage", { playerNames: Array<String>, backgroundImages: Array<Array<Int>> ->
            val backgroundColors = backgroundImages.map { it.toList() }.toList()
            this._userColors.value = playerNames.toList().zip(backgroundColors).map { (name, color) ->
                UserColor(name, Color(color[0], color[1], color[2]))
            }
        }, Array<String>::class.java, Array<Array<Int>>::class.java)
    }

    private fun setupSystemMessage() {
        hubConnection.remove("SystemMessage")
        hubConnection.on("SystemMessage", { content: String ->
            val newSystemMessage = SystemMessage(content)
            _systemMessages.value += newSystemMessage
        }, String::class.java)
    }

    @SuppressLint("CheckResult")
    private fun setupStartGame() {
        hubConnection.remove("GoToGame")
        hubConnection.on("GoToGame", {
            Log.d("SignalR", "GoToGame")
            hubConnection.stop().doOnComplete {
                gameRoomServiceHub.startConnection()
            }.subscribe(
                {
                    Log.d("SignalR", "Hub connection stopped successfully")
                },
                { error ->
                    Log.e("SignalR", "Error stopping hub connection: ${error.message}")
                }
            )
        }, Void::class.java)
    }
    private fun setupTimer() {
        hubConnection.remove("GetTimer")
        hubConnection.on("GetTimer", { counter: Int ->
            _counter.value = counter
        }, Int::class.java)
    }

    fun resetWaitingRoomData() {
        this._waitingRoomDto.value = null
        this._messages.value = emptyList()
        this._userColors.value = emptyList()
        this._isDataLoaded = false
    }

    fun sendPlayerMessage(guid: String, playerId: Int, playerName: String, message: String) {
        if(playerName.isNotBlank() && message.isNotBlank()) {
            hubConnection.send("SendMessage", guid, playerId, playerName, message)
        } else {
            return
        }
    }

    @SuppressLint("CheckResult")
    fun startConnection() {
        Log.d("WaitingRoomViewModel", "Attempting to connect to hub")
        try {
            if(hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
                Log.d("WaitingRoomViewModel", "Starting hub connection")
                hubConnection.start().doOnComplete{
                    Log.d("WaitingRoomViewModel", "Hub connection completed")
                }.doOnError { error ->
                    Log.e("WaitingRoomViewModel", "Hub connection error: ${error.message}")
                }.subscribe(
                    {
                        Log.d("SignalR", "Hub connection stopped successfully")
                    },
                    { error ->
                        Log.e("SignalR", "Error stopping hub connection: ${error.message}")
                    }
                )
            } else {
                Log.d("WaitingRoomViewModel", "Hub already connected: ${hubConnection.connectionState}")
            }
        } catch (e: Exception) {
            Log.e("WaitingRoomViewModel", "Exception in waitingRoomConnection: ${e.message}", e)
        }
    }


    fun sendLeaveRoom(guid: String, playerId: Int) {
        hubConnection.send("LeaveRoom", guid, playerId)
    }

    fun requestJoinGame(guid: String, playerName: String, playerId: Int) {
        clearMessages()
        try {
            if (guid.isBlank()) {
                Log.e("HUB", "Invalid GUID: Empty string")
                return
            }
            if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
                hubConnection.send("JoinGame", guid, playerName, playerId)
                Log.d("HUB", "JoinGame request sent")
                setupReceiveMessage()
            } else {
                Log.w("SignalR", "Error")
            }
        } catch (e: Exception) {
            Log.e("SignalR", "Send error JoinGame: ${e.message}")
        }
    }

    fun requestStartGame(guid: String) {
        if(guid.isNotEmpty() && guid.isNotBlank()) {
            hubConnection.send("StartGame", guid)
        } else {
            Log.w("SignalR", "Cannot requestStartGame now (guid='$guid')")
            return
        }
    }

    fun changePlayerID(playerId: Int) {
        _playerId.value = playerId
    }
    fun stopConnection() {
        hubConnection.stop().subscribe()
    }

    fun reset() {
        _waitingRoomDto.value = null
        _playerId.value = 0
        _messages.value = null
        _systemMessages.value = emptyList()
    }

    fun resetCounter() {
        _counter.value = 35
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

    fun getHubConnection(): HubConnection = hubConnection
}