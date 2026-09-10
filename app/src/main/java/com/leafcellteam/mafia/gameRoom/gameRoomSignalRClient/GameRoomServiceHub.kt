package com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient

import android.content.Context
import android.util.Log
import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.appLanguage.DataStore
import com.leafcellteam.shared.gameRoom.models.AlivePlayersTransmission
import com.leafcellteam.shared.gameRoom.models.GameRoomState
import com.leafcellteam.shared.gameRoom.models.Phase
import com.leafcellteam.shared.gameRoom.models.parseToPhase
import com.leafcellteam.shared.gameRoom.models.Player
import com.leafcellteam.shared.gameRoom.models.PlayerDto
import com.leafcellteam.shared.gameRoom.models.PlayerInfo
import com.leafcellteam.shared.localization.ManualTranslation
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.shared.roles.Role
import com.leafcellteam.shared.waitingRoom.models.ChatMessage
import com.leafcellteam.shared.waitingRoom.models.ChatModel
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

class GameRoomServiceHub(private val scope: CoroutineScope, val context: Context) {
    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${DOMAIN}$HUB")
        .withServerTimeout(30000)
        .build()

    private var _gameRoomState = MutableStateFlow(GameRoomState())
    val gameRoomState: StateFlow<GameRoomState> = _gameRoomState.asStateFlow()

    private var _systemMessage = MutableStateFlow("")
    val systemMessage: StateFlow<String> = _systemMessage.asStateFlow()

    private var _playersInfo = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val playersInfo: StateFlow<List<PlayerInfo>> = _playersInfo.asStateFlow()

    private var _playerId = MutableStateFlow(-1)
    val playerId: StateFlow<Int> = _playerId.asStateFlow()

    private var guid: String = ""

    fun setGuid(newGuid: String) {
        this.guid = newGuid
    }

    fun setupHubConnection() {
        setupSetPhase()
        setupRolesQuantity()
        setupAcquiringGameEvents()
        setupReceiveMessage()
        setupReceivePlayers()
        setupReceiveGameInfo()
        setupGetPlayerId()
        setupPlayerInfoChange()
        setupPlayerDeadChange()
        setupInitialGameRequest()
    }

    fun startConnection() {
        hubConnection.start().subscribe(
            {
                Log.d("SignalR", "Hub connection started successfully")
            },
            { error ->
                Log.e("SignalR", "Error starting hub connection: ${error.message}")
            }
        )
    }

    fun stopConnection(): Disposable {
        return hubConnection.stop().subscribe(
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
            hubConnection.remove("ReceiveRolesQuantity")
            hubConnection.on("ReceiveRolesQuantity", { mafiaQuantity: Int, civilianQuantity: Int, totalAlivePlayers: Int ->
                _gameRoomState.update { state ->
                    state.copy(
                        mafiaQuantity = mafiaQuantity,
                        civilianQuantity = civilianQuantity,
                        totalAlivePlayers = totalAlivePlayers
                    )
                }
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
            hubConnection.on("ReceivePlayerInfo", { playerInfo: PlayerInfo ->
                _playersInfo.update { list ->
                    val newList = list.toMutableList()
                    val index = newList.indexOfFirst { it.playerId == playerInfo.playerId }
                    if (index != -1) {
                        newList[index] = playerInfo
                    } else {
                        newList.add(playerInfo)
                    }
                    newList
                }
            }, PlayerInfo::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "getPlayerInfo: ${e.message}", e)
        }
    }

    private fun setupReceiveMessage() {
        try {
            hubConnection.remove("ReceiveMessage")
            hubConnection.on("ReceiveMessage", { model: ChatModel ->
                _gameRoomState.update { state ->
                    val cp = state.oldPlayers.find { it.playerId == _playerId.value }
                    val chatMessage = ChatMessage(
                        playerId = model.playerId,
                        user = model.playerName,
                        message = model.message,
                        timeStamp = System.currentTimeMillis(),
                        phaseWhenSent = state.phase,
                        currentPlayerWhenSent = cp
                    )
                    
                    val isMafiaMessage = cp != null && (cp.playerRole is Role.Mafia || cp.playerRole is Role.Don || cp.playerRole is Role.Informator || cp.playerRole is Role.Mimic)
                                        && (state.phase == Phase.NightDiscussion)

                    if (isMafiaMessage) {
                        state.copy(mafiaMessages = listOf(chatMessage) + state.mafiaMessages)
                    } else {
                        state.copy(messages = listOf(chatMessage) + state.messages)
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
                    val colorArgb =
                        if (colorList.size >= 3) {
                            (0xFF000000L or (colorList[0].toLong() shl 16) or (colorList[1].toLong() shl 8) or colorList[2].toLong())
                        } else 0xFF808080L

                    Player(dto.playerId, dto.playerName, Role.fromName(dto.playerRole), colorArgb)
                }

                _gameRoomState.update { state ->
                    state.copy(
                        oldPlayers = updatePlayers,
                        playersToVote = state.playersToVote.copy(
                            players = updatePlayers.associate { it.playerId to 0 }
                        ),
                        alivePlayers = updatePlayers.map {
                            AlivePlayersTransmission(
                                it.playerName,
                                it.playerId,
                                true
                            )
                        }
                    )
                }
            }, Array<PlayerDto>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupReceivePlayers: ${e.message}", e)
        }
    }

    private fun setupReceiveGameInfo() {
        try {
            hubConnection.on("ReceiveGameInfo", { receivedGuid: String ->
                this.guid = receivedGuid
                _gameRoomState.update { it.copy(guid = receivedGuid) }
            }, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupReceiveGameInfo: ${e.message}", e)
        }
    }

    private fun setupGetPlayerId() {
        try {
            hubConnection.on("ReceivePlayerId", { id: Int ->
                _playerId.value = id
            }, Int::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupGetPlayerId: ${e.message}", e)
        }
    }

    private fun setupPlayerInfoChange() {
        try {
            hubConnection.on("ReceivePlayerInfoChange", { infos: Array<PlayerInfo> ->
                _playersInfo.update { infos.toList() }
            }, Array<PlayerInfo>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupPlayerInfoChange: ${e.message}", e)
        }
    }

    private fun setupPlayerDeadChange() {
        try {
            hubConnection.on("ReceivePlayerDeadChange", { players: Array<AlivePlayersTransmission> ->
                _gameRoomState.update { it.copy(alivePlayers = players.toList()) }
            }, Array<AlivePlayersTransmission>::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupPlayerDeadChange: ${e.message}", e)
        }
    }

    private fun setupInitialGameRequest() {
        try {
            hubConnection.on("ReceiveInitialGameRequest", { isGameEnd: Boolean, endGameMessage: String ->
                _gameRoomState.update { it.copy(isGameEnd = isGameEnd, endGameMessage = endGameMessage) }
            }, Boolean::class.java, String::class.java)
        } catch (e: Exception) {
            Log.e("GAME_ROOM_SERVICE_HUB", "setupInitialGameRequest: ${e.message}", e)
        }
    }

    fun requestSendMessage(playerId: Int, playerName: String, message: String) {
        hubConnection.send("SendMessage", guid, playerId, playerName, message)
    }

    fun requestJoinGame(playerId: Int, playerName: String) {
        hubConnection.send("JoinGame", guid, playerId, playerName)
    }

    fun requestRolesQuantity() {
        hubConnection.send("GetRolesQuantity", guid)
    }

    fun requestBarmanAbilityUsage(playerId: Int) {
        hubConnection.send("UseBarmanAbility", guid, playerId)
    }

    fun sendJournalistData(firstId: Int, secondId: Int) {
        hubConnection.send("UseJournalistAbility", guid, firstId, secondId)
    }

    fun requestPlayerInfo(currentPlayerId: Int, selectedPlayerId: Int) {
        hubConnection.send("GetPlayerInfo", guid, currentPlayerId, selectedPlayerId)
    }

    fun changeVotes(gameGuid: String, playerId: Int, phase: String) {
        hubConnection.send("ChangeVotes", gameGuid, playerId, phase)
    }

    fun requestLeaveGame(playerId: Int, playerName: String) {
        hubConnection.send("LeaveGame", guid, playerId, playerName)
    }

    fun changePhase(isDay: Boolean) {
        hubConnection.send("ChangePhase", guid, isDay)
    }

    fun requestPhase() {
        hubConnection.send("GetPhase", guid)
    }

    fun resetPlayersInfo() {
        _playersInfo.value = emptyList()
    }

    fun resetGameReceiveMessages() {
        _gameRoomState.update { it.copy(messages = emptyList(), mafiaMessages = emptyList()) }
    }

    fun resetGameEventMessages() {
        _gameRoomState.update { it.copy(gameEvents = emptyList()) }
    }

    fun resetGameRoomState() {
        _gameRoomState.value = GameRoomState()
    }

    fun setupGameCalculation(playerId: Int) {
        hubConnection.send("SetupGameCalculation", guid, playerId)
    }

    fun setupLeaveLogicCalculation(playerId: Int) {
        hubConnection.send("SetupLeaveLogicCalculation", guid, playerId)
    }

    fun disconnectDataSend(playerId: Int) {
        hubConnection.send("DisconnectDataSend", guid, playerId)
    }

    fun getHubConnection(): HubConnection = hubConnection
}
