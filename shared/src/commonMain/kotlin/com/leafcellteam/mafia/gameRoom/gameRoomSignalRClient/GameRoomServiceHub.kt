package com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient

import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.DOMAIN
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
import com.leafcellteam.mafia.network.SignalRHubConnection
import com.leafcellteam.mafia.network.SignalRHubBuilder
import com.leafcellteam.mafia.network.HubState
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import com.leafcellteam.mafia.appLanguage.languages.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.leafcellteam.mafia.network.on

private const val HUB = "mafiaHub"
private const val SERVER_URL = "$DOMAIN$HUB"

class GameRoomServiceHub(
    private val scope: CoroutineScope,
    private val getLanguage: suspend () -> Language
) {
    private val hubConnection: SignalRHubConnection = SignalRHubBuilder(SERVER_URL).build()

    private var _gameRoomState = MutableStateFlow(GameRoomState())
    val gameRoomState: StateFlow<GameRoomState> = _gameRoomState.asStateFlow()

    private var _systemMessage = MutableStateFlow("")
    val systemMessage = _systemMessage.asStateFlow()

    private var _playersInfo = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val playersInfo = _playersInfo.asStateFlow()

    fun startConnection() {
        hubConnection.start()
    }

    private fun setupSetPhase() {
        try {
            hubConnection.remove("SetPhase")
            hubConnection.on("SetPhase") { phase: String ->
                _gameRoomState.update { state ->
                    state.copy(phase = state.phase.parseToPhase(phase))
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupSetPhase: ${e.message}")
        }
    }

    private fun setupRolesQuantity() {
        try {
            hubConnection.remove("ReceiveQuantity")
            hubConnection.on("ReceiveQuantity") { mafiaQuantity: Int, civilianQuantity: Int, totalAlivePlayers: Int ->
                _gameRoomState.value = _gameRoomState.value.copy(
                    mafiaQuantity = mafiaQuantity,
                    civilianQuantity = civilianQuantity,
                    totalAlivePlayers = totalAlivePlayers
                )
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupRolesQuantity: ${e.message}")
        }
    }

    private fun setupAcquiringGameEvents() {
        try {
            hubConnection.remove("AcquireGameEvents")
            hubConnection.on("AcquireGameEvents") { event: String ->
                scope.launch {
                    val lang = getLanguage()
                    _gameRoomState.update { state ->
                        state.copy(
                            gameEvents = ManualTranslation.getEvents(
                                lang = lang,
                                events = listOf(event)
                            ) + state.gameEvents
                        )
                    }
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupAcquiringGameEvents: ${e.message}")
        }
    }

    private fun getPlayerInfo() {
        try {
            hubConnection.on("PlayerInfo") { playerId: Int, isAlive: Boolean, isUnderDoctor: Boolean, isUnderLover: Boolean, isUnderJournalist: Boolean, isUnderDetective: Boolean, isUnderInformator: Boolean, isDonSearch: Boolean ->
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
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "getPlayerInfo: ${e.message}")
        }
    }

    private fun setupSystemMessage() {
        try {
            hubConnection.remove("SystemMessage")
            hubConnection.on("SystemMessage") { content: String ->
                logd("GAME_ROOM_SERVICE_HUB", "SetupSystemMessage: $content")
                _systemMessage.value = content
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupSystemMessage: ${e.message}")
        }
    }

    private fun setupReceiveMessage() {
        try {
            hubConnection.remove("ReceiveMessage")
            hubConnection.on("ReceiveMessage") { chat: ChatModel ->
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

                logd("Check", "isMafiaMessage -> $isMafiaMessage")

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
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupReceiveMessage: ${e.message}")
        }
    }

    /*private fun setupReceivePlayers() {
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

                logd("CheckMap", "playersToVote here -> ${_gameRoomState.value.playersToVote}")
            }, Array::class)
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupReceivePlayers: ${e.message}")
        }
    }*/

    private fun setupReceivePlayers() {
        try {
            hubConnection.remove("ReceiveRoomPlayers")
            hubConnection.on("ReceiveRoomPlayers") { dtos: List<PlayerDto> ->
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

                logd("CheckMap", "playersToVote here -> ${_gameRoomState.value.playersToVote}")
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupReceivePlayers: ${e.message}")
        }
    }

    private fun setupTimer() {
        try {
            hubConnection.remove("ObtainCounter")
            hubConnection.on("ObtainCounter") { counter: Int ->
                _gameRoomState.update { state -> state.copy(timer = counter) }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupTimer: ${e.message}")
        }
    }

    /*private fun setupAlivePlayers() {
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
            }, Array::class, Array::class, Array::class)
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupAlivePlayers: ${e.message}")
        }
    }*/

    private fun setupAlivePlayers() {
        try {
            hubConnection.remove("ReceiveAlivePlayers")
            hubConnection.on("ReceiveAlivePlayers") { playerNames: List<String>, playerIds: List<Int>, isPlayersAlive: List<Boolean> ->
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
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupAlivePlayers: ${e.message}")
        }
    }

    private fun getVotes() {
        try {
            hubConnection.remove("ChangeVotes")
            hubConnection.on("ChangeVotes") { voteNumber: Int, playerId: Int ->
                val temp = _gameRoomState.value.playersToVote.players.toMutableMap()
                temp[playerId] = voteNumber

                _gameRoomState.value = _gameRoomState.value.copy(
                    playersToVote = _gameRoomState.value.playersToVote.copy(players = temp.toMap())
                )
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "getVotes: ${e.message}")
        }
    }

    /*private fun getAlivePlayers() {
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
            }, Array::class, Array::class, Array::class)
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "getAlivePlayers: ${e.message}")
        }
    }*/

    private fun getAlivePlayers() {
        try {
            hubConnection.on("KillPlayer") { playerNames: List<String>, playerIds: List<Int>, isPlayersAlive: List<Boolean> ->
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
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "getAlivePlayers: ${e.message}")
        }
    }

    private fun getJournalistData() {
        try {
            hubConnection.on("JournalistData") { playerId: Int, isUnderJournalist: Boolean ->
                _playersInfo.value = _playersInfo.value.map { playerInfo ->
                    if(playerInfo.playerId == playerId)
                        playerInfo.copy(isUnderJournalist = isUnderJournalist)
                    else
                        playerInfo
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "getJournalistData: ${e.message}")
        }
    }

    private fun monitorGameEnd() {
        try {
            hubConnection.on("MonitorGameEnd") { isGameEnd: Boolean, message: String ->
                scope.launch {
                    val lang = getLanguage()
                    _gameRoomState.value =
                        _gameRoomState.value.copy(
                            isGameEnd = isGameEnd,
                            endGameMessage = ManualTranslation.getEvents(
                                lang = lang,
                                events = listOf(message)
                            ).getOrNull(0) ?: ""
                        )
                }
                logd("GAME_ROOM_SERVICE_HUB", "MonitorGameEnd: isGameEnd = $isGameEnd; message = $message")
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "monitorGameEnd: ${e.message}")
        }
    }

    fun disconnectDataSend(currentPlayerId: Int) {
        try {
            hubConnection.on("DisconnectViaId") { id: Int ->
                if (currentPlayerId != 0 && id != 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            MafiaApi.retrofitService.sendDisconnection(currentPlayerId, id)
                        } catch (e: Exception) {
                            loge("GAME_ROOM_SERVICE_HUB", "sendDisconnection failed: ${e.message}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "disconnectDataSend: ${e.message}")
        }
    }

    fun sendJournalistData(firstSelectedPlayerId: Int, secondSelectedPlayerId: Int) {
        try {
            logd("SendJournalistData", "sendJournalistData: $firstSelectedPlayerId, $secondSelectedPlayerId")
            if(_gameRoomState.value.guid.isNotBlank() && firstSelectedPlayerId != 0 && secondSelectedPlayerId != 0) {
                hubConnection.send(
                    "JournalistFunction",
                    _gameRoomState.value.guid,
                    firstSelectedPlayerId,
                    secondSelectedPlayerId
                )
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "sendJournalistData: ${e.message}")
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
            loge("GAME_ROOM_SERVICE_HUB", "requestPlayerInfo: ${e.message}")
        }
    }

    fun requestBarmanAbilityUsage(playerId: Int) {
        try {
            if (_gameRoomState.value.guid.isNotBlank() && playerId != 0) {
                hubConnection.send("BarmanFunction", _gameRoomState.value.guid, playerId)
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestBarmanAbilityUsage: ${e.message}")
        }
    }

    fun requestPhase() {
        try {
            hubConnection.send("SetPhase", _gameRoomState.value.guid, _gameRoomState.value.phase)
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestPhase: ${e.message}")
        }
    }

    fun changeVotes(gameGuid: String, playerId: Int, phase: String) {
        try {
            hubConnection.send("ChangeVotesByCase", gameGuid, playerId, phase)
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "changeVotes: ${e.message}")
        }
    }

    fun requestLeaveGame(playerId: Int, playerName: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
                hubConnection.send("LeaveGame", _gameRoomState.value.guid, playerId, playerName)
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestLeaveGame: ${e.message}")
        }
    }

    fun requestJoinGame(playerId: Int, playerName: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && playerId != 0) {
                logd("GAME_ROOM_SERVICE_HUB", "requestJoinGame: $playerName")
                logd("GAME_ROOM_SERVICE_HUB", "requestJoinGame: ${_gameRoomState.value.guid}")
                hubConnection.send("JoinGame", _gameRoomState.value.guid, playerName, playerId)
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestJoinGame: ${e.message}")
        }
    }

    fun requestRolesQuantity() {
        try {
            if (_gameRoomState.value.guid.isNotEmpty()) {
                logd("GAME_ROOM_SERVICE_HUB", "requestRolesQuantity: ${_gameRoomState.value.guid}")
                hubConnection.send("TransmitQuantity", _gameRoomState.value.guid)
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestRolesQuantity: ${e.message}")
        }
    }

    fun setupGameCalculation(currentPlayerId: Int) {
        try {
            hubConnection.remove("GameCalculation")
            hubConnection.on("GameCalculation") { isCivilianWon: Boolean ->
                CoroutineScope(Dispatchers.IO).launch {
                    MafiaApi.retrofitService.sendGameCalculation(isCivilianWon, currentPlayerId)
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupGameCalculation: ${e.message}")
        }
    }

    fun setupLeaveLogicCalculation(currentPlayerId: Int) {
        try {
            hubConnection.remove("LeaveLogicCalculation")
            hubConnection.on("LeaveLogicCalculation") { _: Unit ->
                CoroutineScope(Dispatchers.IO).launch {
                    MafiaApi.retrofitService.sendLeaveGameLogic(currentPlayerId)
                }
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "setupLeaveLogicCalculation: ${e.message}")
        }
    }

    fun requestSendMessage(playerId: Int, playerName: String, message: String) {
        try {
            if (_gameRoomState.value.guid.isNotEmpty() && playerName.isNotEmpty() && message.isNotEmpty()) {
                logd("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${_gameRoomState.value.guid}")
                hubConnection.send(
                    "SubmitMessage",
                    _gameRoomState.value.guid,
                    playerId,
                    playerName,
                    message
                )
            }
        } catch (e: Exception) {
            loge("GAME_ROOM_SERVICE_HUB", "requestSendMessage: ${e.message}")
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
            loge("room", "Connection closed: ${it?.message}")
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

    fun getHubConnection(): SignalRHubConnection = hubConnection
    fun stopConnection() {
        hubConnection.stop()
    }
}
