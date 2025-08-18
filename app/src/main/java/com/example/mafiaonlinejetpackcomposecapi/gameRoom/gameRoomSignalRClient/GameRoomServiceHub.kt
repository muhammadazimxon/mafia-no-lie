package com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.mafiaonlinejetpackcomposecapi.HOST_1
import com.example.mafiaonlinejetpackcomposecapi.PORT_2
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.models.VotePlayers
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.ChatMessage
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.ChatModel
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val HUB = "mafiaHub"

private const val SERVER_URL = "http://${HOST_1}:${PORT_2}/$HUB"

data class Player(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: Role = Role.Civilian,
    val avatarColor: Color = Color.Unspecified,
)
data class AlivePlayersTransmission(
    val playerName: String,
    val playerId: Int,
    val isAlive: Boolean
)
data class PlayerInfo(
    val playerId: Int,
    val isAlive: Boolean,
    val isUnderLover: Boolean,
    val isUnderDoctor: Boolean,
    val isUnderJournalist: Boolean,
    val isUnderDetective: Boolean,
    val isUnderInformator: Boolean,
    val isDonSearch: Boolean
)

data class PlayerDto(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: String = "",
    val avatarColor: Array<Int> = emptyArray<Int>()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerDto

        if (playerId != other.playerId) return false
        if (playerName != other.playerName) return false
        if (playerRole != other.playerRole) return false
        if (!avatarColor.contentEquals(other.avatarColor)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playerId
        result = 31 * result + playerName.hashCode()
        result = 31 * result + playerRole.hashCode()
        result = 31 * result + avatarColor.contentHashCode()
        return result
    }
}

enum class Phase {
    NightDiscussion, NightVote, DayDiscussion, DayVote, None;

    fun parseToPhase(phase: String): Phase {
        return when(phase) {
            "NightDiscussion" -> NightDiscussion
            "NightVote" -> NightVote
            "DayDiscussion" -> DayDiscussion
            "DayVote" -> DayVote
            else -> DayVote
        }
    }
}

data class GameRoomState(
    val guid: String = "",
    val timer: Int = -1,
    val mafiaQuantity: Int = 0,
    val civilianQuantity: Int = 0,
    val totalAlivePlayers: Int = 0,
    val phase: Phase = Phase.None,
    val gameEvents: List<String> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val mafiaMessages: List<ChatMessage> = emptyList(),
    val oldPlayers: List<Player> = emptyList(),
    var alivePlayers: List<AlivePlayersTransmission> = emptyList(),
    var playersToVote: VotePlayers = VotePlayers(emptyMap()),
    val isGameEnd: Boolean = false,
    val endGameMessage: String = ""
)

class GameRoomServiceHub {
    private val hubConnection: HubConnection = HubConnectionBuilder
        .create(SERVER_URL)
        .withHandshakeResponseTimeout(30000)
        .build()

    private var _gameRoomState = MutableStateFlow(GameRoomState())
    val gameRoomState: StateFlow<GameRoomState> = _gameRoomState.asStateFlow()

    private var _systemMessage = MutableStateFlow("")
    val systemMessage = _systemMessage.asStateFlow()

    private var _playersInfo = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val playersInfo = _playersInfo.asStateFlow()

    fun startConnection() {
        hubConnection.start().subscribe(
            {
                Log.d("SignalR", "Hub connection stopped successfully")
            },
            { error ->
                Log.e("SignalR", "Error stopping hub connection: ${error.message}")
            }
        )
    }

    private fun setupSetPhase() {
        hubConnection.remove("SetPhase")
        hubConnection.on("SetPhase", { phase: String ->
            _gameRoomState.update { state ->
                state.copy(phase = state.phase.parseToPhase(phase))
            }
        }, String::class.java)
    }

    private fun setupRolesQuantity() {
        hubConnection.remove("ReceiveQuantity")
        hubConnection.on("ReceiveQuantity", { mafiaQuantity: Int, civilianQuantity: Int, totalAlivePlayers: Int ->
            _gameRoomState.value = _gameRoomState.value.copy(
                mafiaQuantity = mafiaQuantity,
                civilianQuantity = civilianQuantity,
                totalAlivePlayers = totalAlivePlayers
            )
        }, Int::class.java, Int::class.java, Int::class.java)
    }

    private fun setupAcquiringGameEvents() {
        hubConnection.remove("AcquireGameEvents")
        hubConnection.on("AcquireGameEvents", { event: String ->
            _gameRoomState.update { state -> state.copy(gameEvents = listOf(event) + state.gameEvents) }
        }, String::class.java)
    }

    private fun getPlayerInfo() {
        hubConnection.on("PlayerInfo", {
            playerId: Int,
            isAlive: Boolean,
            isUnderDoctor: Boolean,
            isUnderLover: Boolean,
            isUnderJournalist: Boolean,
            isUnderDetective: Boolean,
            isUnderInformator: Boolean,
            isDonSearch: Boolean ->
            if( !_playersInfo.value.any { p -> p.playerId == playerId } ) {
                _playersInfo.value += PlayerInfo(
                    playerId = playerId,
                    isUnderLover = isUnderLover,
                    isUnderDoctor = isUnderDoctor,
                    isAlive = isAlive,
                    isUnderJournalist = isUnderJournalist,
                    isUnderDetective = isUnderDetective,
                    isUnderInformator = isUnderInformator,
                    isDonSearch = isDonSearch
                )
            } else {
                _playersInfo.value = _playersInfo.value.map { p -> p.copy(
                    playerId = playerId,
                    isUnderLover = isUnderLover,
                    isUnderDoctor = isUnderDoctor,
                    isAlive = isAlive,
                    isUnderJournalist = isUnderJournalist,
                    isUnderDetective = isUnderDetective,
                    isUnderInformator = isUnderInformator,
                    isDonSearch = isDonSearch
                ) }
            }
        },
            Int::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java,
            Boolean::class.java
        )
    }

    private fun setupSystemMessage() {
        hubConnection.remove("SystemMessage")
        hubConnection.on("SystemMessage", { content: String ->
            Log.d("GAME_ROOM_SERVICE_HUB", "SetupSystemMessage: $content")
            _systemMessage.value = content
        }, String::class.java)
    }

    private fun setupReceiveMessage() {
        hubConnection.remove("ReceiveMessage")
        hubConnection.on("ReceiveMessage", { chat: ChatModel ->
            val chatMessage = ChatMessage(
                playerId = chat.playerId,
                user = chat.playerName,
                message = chat.message,
                phaseWhenSent = _gameRoomState.value.phase,
                currentPlayerWhenSent = _gameRoomState.value.oldPlayers.find { it.playerId == chat.playerId }
            )

            val isMafiaMessage = _gameRoomState.value.oldPlayers
                .find { it.playerId == chat.playerId }
                ?.playerRole?.let { role ->
                    role is Role.Mafia || role is Role.Don || role is Role.Informator || role is Role.Mimic
                } ?: false

            Log.d("Check", "isMafiaMessage -> $isMafiaMessage")

            if(_gameRoomState.value.phase == Phase.NightDiscussion && isMafiaMessage) {
                _gameRoomState.update { state ->
                    state.copy(mafiaMessages = state.mafiaMessages + chatMessage)
                }
            } else if(_gameRoomState.value.phase == Phase.DayDiscussion) {
                _gameRoomState.update { state ->
                    state.copy(messages = state.messages + chatMessage)
                }
            }

            if(chatMessage.playerId == -1) {
                _gameRoomState.update { state ->
                    state.copy(messages = state.messages + chatMessage)
                }
            }

        }, ChatModel::class.java)
    }

    private fun setupReceivePlayers() {
        hubConnection.remove("ReceiveRoomPlayers")
        hubConnection.on("ReceiveRoomPlayers", { dtos: Array<PlayerDto> ->
            val updatePlayers = dtos.map { dto ->
                val colorList = dto.avatarColor
                val color = if (colorList.size >= 3)
                    Color(colorList[0], colorList[1], colorList[2])
                else Color.Gray
                Player(
                    dto.playerId,
                    dto.playerName,
                    Role.fromName(dto.playerRole),
                    color
                )
            }
            _gameRoomState.update { it.copy(oldPlayers = updatePlayers) }
            _gameRoomState.value.playersToVote.players = _gameRoomState.value.oldPlayers.associate { p -> p.playerId to 0 }
            _gameRoomState.value.oldPlayers.forEach { player ->
                Log.d(
                    "checkIDS",
                    "OldplayerId -> ${player.playerId}"
                )
            }
            _gameRoomState.value.alivePlayers = _gameRoomState.value.oldPlayers.map { AlivePlayersTransmission(it.playerName, it.playerId, true) }
            Log.d("CheckMap", "playersToVote here -> ${_gameRoomState.value.playersToVote}")

        }, Array<PlayerDto>::class.java)
    }

    private fun setupTimer() {
        hubConnection.remove("ObtainCounter")
        hubConnection.on("ObtainCounter", { counter: Int ->
            _gameRoomState.update { state -> state.copy(timer = counter) }
        }, Int::class.java)
    }

    private fun setupAlivePlayers() {
        hubConnection.remove("ReceiveAlivePlayers")
        hubConnection.on("ReceiveAlivePlayers", { playerNames: Array<String>, playerIds: Array<Int>, isPlayersAlive: Array<Boolean> ->
            Log.d("GAME_ROOM_SERVICE_HUB", "SetupAlivePlayers: ${playerNames.joinToString(" ")}")
            Log.d("GAME_ROOM_SERVICE_HUB", "SetupAlivePlayers: ${playerIds.joinToString(" ")}")
            Log.d("GAME_ROOM_SERVICE_HUB", "SetupAlivePlayers: ${isPlayersAlive.joinToString(" ")}")

            _gameRoomState.update { state ->
                state.copy(
                    alivePlayers = playerNames.mapIndexed { idx, name ->
                        AlivePlayersTransmission(
                            playerName = name,
                            playerId   = playerIds.getOrNull(idx) ?: 0,
                            isAlive    = isPlayersAlive.getOrNull(idx) ?: false
                        )
                    }
                )
            }

            _gameRoomState.value.alivePlayers.forEach { player ->
                Log.d("checkIDS", "AliveplayerId -> ${player.playerId}")
            }
        }, Array<String>::class.java, Array<Int>::class.java, Array<Boolean>::class.java)
    }

    private fun getVotes() {
        hubConnection.remove("ChangeVotes")
        hubConnection.on("ChangeVotes", { voteNumber: Int, playerId: Int ->
            val temp = _gameRoomState.value.playersToVote.players.toMutableMap()
            temp[playerId] = voteNumber
            _gameRoomState.value = _gameRoomState.value.copy(
                playersToVote = _gameRoomState.value.playersToVote.copy(
                    players = temp.toMap()
                )
            )
        }, Int::class.java, Int::class.java)
    }

    private fun getAlivePlayers() {
        hubConnection.on("KillPlayer", { playerNames: Array<String>, playerIds: Array<Int>, isPlayersAlive: Array<Boolean> ->
            Log.d("alivePlayersCheck", playerNames.toList().toString())
            println(playerNames)
            Log.d("alivePlayersCheck", playerIds.toList().toString())
            println(playerIds)
            Log.d("alivePlayersCheck", isPlayersAlive.toList().toString())
            println(isPlayersAlive)

            _gameRoomState.update { state ->
                state.copy(
                    alivePlayers = playerNames.mapIndexed { ind, name ->
                        AlivePlayersTransmission(
                            playerName = name,
                            playerId = playerIds[ind],
                            isAlive = isPlayersAlive[ind])
                    }
                )
            }
            _gameRoomState.value.alivePlayers.forEach { player ->
                Log.d(
                    "checkIDS",
                    "Alive playerId -> ${player.playerId}"
                )
            }
        }, Array<String>::class.java, Array<Int>::class.java, Array<Boolean>::class.java)
    }

    private fun getJournalistData() {
        hubConnection.on("JournalistData", { playerId: Int, isUnderJournalist: Boolean ->
            _playersInfo.value = _playersInfo.value.map { playerInfo ->
                if(playerInfo.playerId == playerId)
                    playerInfo.copy(isUnderJournalist = isUnderJournalist)
                else
                    playerInfo
            }
        }, Int::class.java, Boolean::class.java)
    }

    private fun monitorGameEnd() {
        hubConnection.on("MonitorGameEnd", { isGameEnd: Boolean, message: String ->
            _gameRoomState.value =
                _gameRoomState.value.copy(
                    isGameEnd = isGameEnd,
                    endGameMessage = message
                )
            Log.d("GAME_ROOM_SERVICE_HUB",
                "MonitorGameEnd: isGameEnd = $isGameEnd; message = $message")

        }, Boolean::class.java, String::class.java)
    }

    fun disconnectDataSend(currentPlayerId: Int) {
        hubConnection.on("DisconnectViaId", { id: Int ->
            CoroutineScope(Dispatchers.IO).launch {
                MafiaApi.retrofitService.sendDisconnection(currentPlayerId, id)
            }
        }, Int::class.java)
    }

    fun sendJournalistData(firstSelectedPlayerId: Int, secondSelectedPlayerId: Int) {
        Log.e("SendJournalistData", "sendJournalistData: $firstSelectedPlayerId, $secondSelectedPlayerId", )
        if(_gameRoomState.value.guid.isNotBlank() && firstSelectedPlayerId != 0 && secondSelectedPlayerId != 0) {
            hubConnection.send("JournalistFunction", _gameRoomState.value.guid, firstSelectedPlayerId, secondSelectedPlayerId)
        }
    }

    fun requestPlayerInfo(currentPlayerId: Int, playerId: Int) {
        if( _gameRoomState.value.guid.isNotBlank() && currentPlayerId != 0 && playerId != 0 ) {
            hubConnection.send("PlayerInfo",
                _gameRoomState.value.guid,
                currentPlayerId,
                playerId
            )
        }
    }

    fun requestBarmanAbilityUsage(playerId: Int) {
        if (_gameRoomState.value.guid.isNotBlank() && playerId != 0) {
            hubConnection.send("BarmanFunction", _gameRoomState.value.guid, playerId)
        }
    }

    fun requestPhase() {
        hubConnection.send("SetPhase", _gameRoomState.value.guid, _gameRoomState.value.phase)
    }

    fun changeVotes(gameGuid: String, playerId: Int, phase: String) {
        hubConnection.send("ChangeVotesByCase", gameGuid, playerId, phase)
    }

    fun requestLeaveGame(playerId: Int, playerName: String) {
        if(_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
            hubConnection.send("LeaveGame", _gameRoomState.value.guid, playerId, playerName)
        }
    }

    fun requestJoinGame(playerId: Int, playerName: String) {
        if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestJoinGame: $playerName")
            Log.e("GAME_ROOM_SERVICE_HUB", "requestJoinGame: ${_gameRoomState.value.guid}")
            hubConnection.send("JoinGame", _gameRoomState.value.guid, playerName, playerId)
        }
    }

    fun requestRolesQuantity() {
        if (_gameRoomState.value.guid.isNotEmpty()) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestRolesQuantity: ${_gameRoomState.value.guid}")
            hubConnection.send("TransmitQuantity", _gameRoomState.value.guid)
        }
    }

    fun setupGameCalculation(currentPlayerId: Int) {
        hubConnection.remove("GameCalculation")
        hubConnection.on("GameCalculation", { isCivilianWon: Boolean ->
            CoroutineScope(Dispatchers.IO).launch {
                MafiaApi.retrofitService.sendGameCalculation(isCivilianWon, currentPlayerId)
            }
        }, Boolean::class.java)
    }

    fun setupLeaveLogicCalculation(currentPlayerId: Int) {
        hubConnection.remove("LeaveLogicCalculation")
        hubConnection.on("LeaveLogicCalculation", {
            CoroutineScope(Dispatchers.IO).launch {
                MafiaApi.retrofitService.sendLeaveGameLogic(currentPlayerId)
            }
        })
    }

    fun requestSendMessage(playerId: Int, playerName: String, message: String) {
        if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && message.isNotEmpty()) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestRemitGameEvent: ${_gameRoomState.value.guid}")
            Log.e("GAME_ROOM_SERVICE_HUB", "requestRemitGameEvent: $playerName")
            Log.e("GAME_ROOM_SERVICE_HUB", "requestRemitGameEvent: $message")
            hubConnection.send("SubmitMessage", _gameRoomState.value.guid, playerId, playerName, message)
        }
    }

    fun resetPlayersInfo() {
        _playersInfo.value = emptyList()
    }

    fun resetGameReceiveMessages() {
        _gameRoomState.update { state ->
            state.copy(
                messages = emptyList(),
                mafiaMessages = emptyList()
            )
        }
    }

    fun resetGameRoomState() {
        _gameRoomState.value = GameRoomState()
    }


    fun resetGameEventMessages() {
        _gameRoomState.update { state ->
            state.copy(
                gameEvents = emptyList()
            )
        }
    }

    fun setupHubConnection() {
        hubConnection.onClosed {
            Log.e("room", "Connection closed: ${it?.message}")
        }
        getJournalistData()
        getPlayerInfo()
        getAlivePlayers()
        setupTimer()
        setupSetPhase()
        setupRolesQuantity()
        setupSystemMessage()
        getVotes()
        setupAlivePlayers()
        setupReceiveMessage()
        setupReceivePlayers()
        setupAcquiringGameEvents()
        monitorGameEnd()
    }

    fun setGuid(guid: String) {
        _gameRoomState.value = _gameRoomState.value.copy(guid = guid)
    }

    fun changePhase(isDay: Boolean) {
        _gameRoomState.value = _gameRoomState.value.copy(
            phase = if (isDay) Phase.DayDiscussion else Phase.NightDiscussion
        )
    }

    fun getHubConnection(): HubConnection = hubConnection
    fun stopConnection(): Disposable = hubConnection.stop().subscribe(
        {
            Log.d("SignalR", "Hub connection stopped successfully")
        },
        { error ->
            Log.e("SignalR", "Error stopping hub connection: ${error.message}")
        }
    )
}