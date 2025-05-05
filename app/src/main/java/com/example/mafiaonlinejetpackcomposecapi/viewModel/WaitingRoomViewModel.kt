package com.example.mafiaonlinejetpackcomposecapi.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mafiaonlinejetpackcomposecapi.waitingRoomModels.waitingRoomDto.WaitingRoomDto
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

private const val SERVER_URL = "http://10.0.2.2:5020/waitingRoomHub"

data class ChatMessage(
    val user: String,
    val message: String,
    val timeStamp: Date = Date()
)
data class ChatModel(
    val playerName: String,
    val message: String
)
data class SystemMessage(
    val content: String,
    val temStamp: Date = Date()
)

class WaitingRoomViewModel : ViewModel() {
    val hubConnection: HubConnection = HubConnectionBuilder
        .create(SERVER_URL)
        .build()

    var guid by mutableStateOf("")
    var playerId by mutableIntStateOf(0)

    var messageState by mutableStateOf("")
    var gameCounter by mutableIntStateOf(35)
    var currentPlayer by mutableStateOf("")

    private val _isStartState = MutableStateFlow(false)
    val isStartState: StateFlow<Boolean> = _isStartState.asStateFlow()

    private val _currentPlayersNumber = MutableStateFlow(1)
    val currentPlayersNumber: StateFlow<Int> = _currentPlayersNumber.asStateFlow()

    private val _messages = MutableStateFlow(emptyList<ChatMessage>())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _systemMessages = MutableStateFlow(emptyList<SystemMessage>())
    val systemMessages: StateFlow<List<SystemMessage>> = _systemMessages.asStateFlow()

    private val _isDataLoaded = mutableStateOf(false)

    private val _waitingRoomDto = MutableStateFlow<WaitingRoomDto?>(null)
    val waitingRoomDto: StateFlow<WaitingRoomDto?> = _waitingRoomDto.asStateFlow()

    fun startConnection() {
        Log.d("WaitingRoomViewModel", "Attempting to connect to hub")
        try {
            if(hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
                Log.d("WaitingRoomViewModel", "Starting hub connection")
                hubConnection.start().doOnComplete{
                    Log.d("WaitingRoomViewModel", "Hub connection completed")
                }.doOnError { error ->
                    Log.e("WaitingRoomViewModel", "Hub connection error: ${error.message}")
                }.subscribe()
            } else {
                Log.d("WaitingRoomViewModel", "Hub already connected: ${hubConnection.connectionState}")
            }
        } catch (e: Exception) {
            Log.e("WaitingRoomViewModel", "Exception in waitingRoomConnection: ${e.message}", e)
        }
    }
    fun receiveWaitingRoomData() {
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
            _isDataLoaded.value = true
        },
            String::class.java,
            String::class.java,
            Array<String>::class.java,
            Array<String>::class.java,
            Int::class.java,
            Int::class.java
        )
    }
    fun setupReceivePlayers() {
        hubConnection.on("ReceivePlayers", { players: Array<String> ->
            _waitingRoomDto.value = _waitingRoomDto.value?.copy(players = players.toList()) ?: _waitingRoomDto.value
        }, Array<String>::class.java)
    }
    fun setupReceiveMessage() {
        hubConnection.on("ReceiveMessage", { chatModels: Array<ChatModel?> ->
            val updatedChatModels = chatModels.map { ChatMessage(it?.playerName ?: "Loading...", it?.message ?: "") }
            _messages.value = updatedChatModels
        }, Array<ChatModel?>::class.java)
    }
    fun sendPlayerMessage(guid: String, playerName: String, message: String) {
        if(playerName.isNotBlank() || message.isNotBlank()) {
            hubConnection.send("SendMessage", guid, playerName, message)
        } else {
            return
        }
    }
    fun sendLeaveRoom(guid: String, playerId: Int) {
        hubConnection.send("LeaveRoom", guid, playerId)
    }
    fun clearMessages() {
        _messages.value = emptyList()
    }
    fun requestJoinGame(guid: String, playerName: String) {
        clearMessages()
        try {
            if (guid.isBlank()) {
                Log.e("HUB", "Invalid GUID: Empty string")
                return
            }
            if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
                hubConnection.send("JoinGame", guid, playerName)
                Log.d("HUB", "JoinGame request sent")
                setupReceiveMessage()
            } else {
                Log.w("SignalR", "Error")
            }
        } catch (e: Exception) {
            Log.e("SignalR", "Send error JoinGame: ${e.message}")
        }
    }
    fun setupSystemMessage() {
        hubConnection.on("SystemMessage", { content: String ->
            val newSystemMessage = SystemMessage(content)
            _systemMessages.value = _systemMessages.value + newSystemMessage
        }, String::class.java)
    }
    fun requestIsStartGame() {
        if(hubConnection.connectionState == HubConnectionState.CONNECTED) {
            hubConnection.send("IsStartState", guid)
        } else {
            Log.w("SignalR", "Cannot requestIsStartState now (state=${hubConnection.connectionState}, guid='$guid')")
        }
    }
    fun setupIsStartGame() {
        hubConnection.on("isStartState", { isStart: Boolean ->
            _isStartState.value = isStart
        }, Boolean::class.java)
    }
    override fun onCleared() {
        hubConnection.stop()
        super.onCleared()
    }
}