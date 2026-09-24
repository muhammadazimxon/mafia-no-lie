package com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.appLanguage.DataStore
import com.leafcellteam.mafia.gameRoom.models.AlivePlayersTransmission
import com.leafcellteam.mafia.gameRoom.models.GameRoomState
import com.leafcellteam.mafia.gameRoom.models.Phase
import com.leafcellteam.mafia.gameRoom.models.Player
import com.leafcellteam.mafia.gameRoom.models.PlayerDto
import com.leafcellteam.mafia.gameRoom.models.PlayerInfo
import com.leafcellteam.mafia.manualTranslation.ManualTranslation
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.roles.Role
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatMessage
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatModel
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

private const val SERVER_URL = "$DOMAIN$HUB"

class GameRoomServiceHub(private val scope: CoroutineScope, val context: Context) {
    private val hubConnection: HubConnection = HubConnectionBuilder
        .create(SERVER_URL)
        .withServerTimeout(30000)
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
        try {
            hubConnection.remove("SetPhase")
            hubConnection.on("SetPhase", { phase: String ->
                _gameRoomState.update { state ->
                    state.copy(phase = state.phase.parseToPhase(phase))
                }
            }, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupSetPhase: ${e.message}", e)
        }
    }

    private fun setupRolesQuantity() {
        try {
            hubConnection.remove("ReceiveQuantity")
            hubConnection.on("ReceiveQuantity", { mafiaQuantity: Int, civilianQuantity: Int, totalAlivePlayers: Int ->
                _gameRoomState.value = _gameRoomState.value.copy(
                    mafiaQuantity = mafiaQuantity,
                    civilianQuantity = civilianQuantity,
                    totalAlivePlayers = totalAlivePlayers
                )
            }, Int::class.java, Int::class.java, Int::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupRolesQuantity: ${e.message}", e)
        }
    }

    private fun setupAcquiringGameEvents() {
        try {
            hubConnection.remove("AcquireGameEvents")
            hubConnection.on("AcquireGameEvents", { event: String ->
                scope.launch {
                    _gameRoomState.update { state ->
                        state.copy(
                            gameEvents = ManualTranslation.getEvents(
                                lang = DataStore.getLanguage(
                                    context = context
                                ), events = listOf(event)
                            ) + state.gameEvents
                        )
                    }
                }
            }, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupAcquiringGameEvents: ${e.message}", e)
        }
    }

    private fun getPlayerInfo() {
        try {
            hubConnection.on("PlayerInfo", {
                    playerId: Int,
                    isAlive: Boolean,
                    isUnderDoctor: Boolean,
                    isUnderLover: Boolean,
                    isUnderJournalist: Boolean,
                    isUnderDetective: Boolean,
                    isUnderInformator: Boolean,
                    isDonSearch: Boolean ->

                if (!_playersInfo.value.any { p -> p.playerId == playerId }) {
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
                    _playersInfo.value = _playersInfo.value.map { p ->
                        p.copy(
                            playerId = playerId,
                            isUnderLover = isUnderLover,
                            isUnderDoctor = isUnderDoctor,
                            isAlive = isAlive,
                            isUnderJournalist = isUnderJournalist,
                            isUnderDetective = isUnderDetective,
                            isUnderInformator = isUnderInformator,
                            isDonSearch = isDonSearch
                        )
                    }
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
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "getPlayerInfo: ${e.message}", e)
        }
    }

    private fun setupSystemMessage() {
        try {
            hubConnection.remove("SystemMessage")
            hubConnection.on("SystemMessage", { content: String ->
                Log.d("GAME_ROOM_SERVICE_HUB", "SetupSystemMessage: $content")
                _systemMessage.value = content
            }, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupSystemMessage: ${e.message}", e)
        }
    }

    private fun setupReceiveMessage() {
        try {
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

                if (_gameRoomState.value.phase == Phase.NightDiscussion && isMafiaMessage) {
                    _gameRoomState.update { state ->
                        state.copy(mafiaMessages = state.mafiaMessages + chatMessage)
                    }
                } else if (_gameRoomState.value.phase == Phase.DayDiscussion) {
                    _gameRoomState.update { state ->
                        state.copy(messages = state.messages + chatMessage)
                    }
                }

                if (chatMessage.playerId == -1) {
                    _gameRoomState.update { state ->
                        state.copy(messages = state.messages + chatMessage)
                    }
                }
            }, ChatModel::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupReceiveMessage: ${e.message}", e)
        }
    }

    private fun setupReceivePlayers() {
        try {
            hubConnection.remove("ReceiveRoomPlayers")
            hubConnection.on("ReceiveRoomPlayers", { dtos: Array<PlayerDto> ->
                val updatePlayers = dtos.map { dto ->
                    val colorList = dto.avatarColor
                    val color =
                        if (colorList.size >= 3) Color(colorList[0], colorList[1], colorList[2])
                        else Color.Gray

                    Player(dto.playerId, dto.playerName, Role.fromName(dto.playerRole), color)
                }

                _gameRoomState.update { it.copy(oldPlayers = updatePlayers) }
                _gameRoomState.value.playersToVote.players =
                    _gameRoomState.value.oldPlayers.associate { p -> p.playerId to 0 }

                _gameRoomState.value.alivePlayers =
                    _gameRoomState.value.oldPlayers.map {
                        AlivePlayersTransmission(
                            it.playerName,
                            it.playerId,
                            true
                        )
                    }

                Log.d("CheckMap", "playersToVote here -> ${_gameRoomState.value.playersToVote}")
            }, Array<PlayerDto>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupReceivePlayers: ${e.message}", e)
        }
    }

    private fun setupTimer() {
        try {
            hubConnection.remove("ObtainCounter")
            hubConnection.on("ObtainCounter", { counter: Int ->
                _gameRoomState.update { state -> state.copy(timer = counter) }
            }, Int::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupTimer: ${e.message}", e)
        }
    }

    private fun setupAlivePlayers() {
        try {
            hubConnection.remove("ReceiveAlivePlayers")
            hubConnection.on("ReceiveAlivePlayers", { playerNames: Array<String>, playerIds: Array<Int>, isPlayersAlive: Array<Boolean> ->

                _gameRoomState.update { state ->
                    state.copy(
                        alivePlayers = playerNames.mapIndexed { idx, name ->
                            AlivePlayersTransmission(
                                playerName = name,
                                playerId = playerIds.getOrNull(idx) ?: 0,
                                isAlive = isPlayersAlive.getOrNull(idx) ?: false
                            )
                        }
                    )
                }
            }, Array<String>::class.java, Array<Int>::class.java, Array<Boolean>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupAlivePlayers: ${e.message}", e)
        }
    }

    private fun getVotes() {
        try {
            hubConnection.remove("ChangeVotes")
            hubConnection.on("ChangeVotes", { voteNumber: Int, playerId: Int ->
                val temp = _gameRoomState.value.playersToVote.players.toMutableMap()
                temp[playerId] = voteNumber

                _gameRoomState.value = _gameRoomState.value.copy(
                    playersToVote = _gameRoomState.value.playersToVote.copy(players = temp.toMap())
                )
            }, Int::class.java, Int::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "getVotes: ${e.message}", e)
        }
    }

    private fun getAlivePlayers() {
        try {
            hubConnection.on("KillPlayer", { playerNames: Array<String>, playerIds: Array<Int>, isPlayersAlive: Array<Boolean> ->

                _gameRoomState.update { state ->
                    state.copy(
                        alivePlayers = playerNames.mapIndexed { index, name ->
                            AlivePlayersTransmission(
                                playerName = name,
                                playerId = playerIds[index],
                                isAlive = isPlayersAlive[index]
                            )
                        }
                    )
                }
            }, Array<String>::class.java, Array<Int>::class.java, Array<Boolean>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "getAlivePlayers: ${e.message}", e)
        }
    }

    private fun getJournalistData() {
        try {
            hubConnection.on("JournalistData", { playerId: Int, isUnderJournalist: Boolean ->
                _playersInfo.value = _playersInfo.value.map { playerInfo ->
                    if(playerInfo.playerId == playerId)
                        playerInfo.copy(isUnderJournalist = isUnderJournalist)
                    else
                        playerInfo
                }
            }, Int::class.java, Boolean::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    private fun monitorGameEnd() {
        try {
            hubConnection.on("MonitorGameEnd", { isGameEnd: Boolean, message: String ->
                scope.launch {
                    _gameRoomState.value =
                        _gameRoomState.value.copy(
                            isGameEnd = isGameEnd,
                            endGameMessage = ManualTranslation.getEvents(
                                lang = DataStore.getLanguage(
                                    context = context
                                ), events = listOf(message)
                            )[0]
                        )
                }
                Log.d("GAME_ROOM_SERVICE_HUB",
                    "MonitorGameEnd: isGameEnd = $isGameEnd; message = $message")

            }, Boolean::class.java, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun disconnectDataSend(currentPlayerId: Int) {
        try {
            hubConnection.on("DisconnectViaId", { id: Int ->
                if (currentPlayerId != 0 && id != 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            MafiaApi.retrofitService.sendDisconnection(currentPlayerId, id)
                        } catch (e: Exception) {
                            Log.e("GAME_ROOM_SERVICE_HUB", "sendDisconnection failed: ${e.message}", e)
                        }
                    }
                }
            }, Int::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "disconnectDataSend: ${e.message}", e)
        }
    }

    fun sendJournalistData(firstSelectedPlayerId: Int, secondSelectedPlayerId: Int) {
        try {
            Log.e("SendJournalistData", "sendJournalistData: $firstSelectedPlayerId, $secondSelectedPlayerId", )
            if(_gameRoomState.value.guid.isNotBlank() && firstSelectedPlayerId != 0 && secondSelectedPlayerId != 0) {
                hubConnection.send(
                    "JournalistFunction",
                    _gameRoomState.value.guid,
                    firstSelectedPlayerId,
                    secondSelectedPlayerId
                )
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestPlayerInfo(currentPlayerId: Int, playerId: Int) {
        try {
            if( _gameRoomState.value.guid.isNotBlank() && currentPlayerId != 0 && playerId != 0 ) {
                hubConnection.send(
                    "PlayerInfo",
                    _gameRoomState.value.guid,
                    currentPlayerId,
                    playerId
                )
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestBarmanAbilityUsage(playerId: Int) {
        try {
            if (_gameRoomState.value.guid.isNotBlank() && playerId != 0) {
                hubConnection.send("BarmanFunction", _gameRoomState.value.guid, playerId)
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestPhase() {
        try {
            hubConnection.send("SetPhase", _gameRoomState.value.guid, _gameRoomState.value.phase)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun changeVotes(gameGuid: String, playerId: Int, phase: String) {
        try {
            hubConnection.send("ChangeVotesByCase", gameGuid, playerId, phase)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestLeaveGame(playerId: Int, playerName: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
                hubConnection.send("LeaveGame", _gameRoomState.value.guid, playerId, playerName)
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestJoinGame(playerId: Int, playerName: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
                Log.e("GAME_ROOM_SERVICE_HUB", "requestJoinGame: $playerName")
                Log.e("GAME_ROOM_SERVICE_HUB", "requestJoinGame: ${_gameRoomState.value.guid}")
                hubConnection.send("JoinGame", _gameRoomState.value.guid, playerName, playerId)
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestRolesQuantity() {
        try {
            if (_gameRoomState.value.guid.isNotEmpty()) {
                Log.e("GAME_ROOM_SERVICE_HUB", "requestRolesQuantity: ${_gameRoomState.value.guid}")
                hubConnection.send("TransmitQuantity", _gameRoomState.value.guid)
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun setupGameCalculation(currentPlayerId: Int) {
        try {
            hubConnection.remove("GameCalculation")
            hubConnection.on("GameCalculation", { isCivilianWon: Boolean ->
                CoroutineScope(Dispatchers.IO).launch {
                    MafiaApi.retrofitService.sendGameCalculation(isCivilianWon, currentPlayerId)
                }
            }, Boolean::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun setupLeaveLogicCalculation(currentPlayerId: Int) {
        try {
            hubConnection.remove("LeaveLogicCalculation")
            hubConnection.on("LeaveLogicCalculation", {
                CoroutineScope(Dispatchers.IO).launch {
                    MafiaApi.retrofitService.sendLeaveGameLogic(currentPlayerId)
                }
            })
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
        }
    }

    fun requestSendMessage(playerId: Int, playerName: String, message: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && message.isNotEmpty()) {
                Log.e(
                    "GAME_ROOM_SERVICE_HUB",
                    "requestRemitGameEvent: ${_gameRoomState.value.guid}"
                )
                Log.e("GAME_ROOM_SERVICE_HUB", "requestRemitGameEvent: $playerName")
                Log.e("GAME_ROOM_SERVICE_HUB", "requestRemitGameEvent: $message")
                hubConnection.send(
                    "SubmitMessage",
                    _gameRoomState.value.guid,
                    playerId,
                    playerName,
                    message
                )
            }
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}", )
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