package com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Phase
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.models.VotePlayers
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.BarmanDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.BarmanHandler
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.BomberDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.BooleanEvents
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.DetectiveHandler
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.DoctorDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.DonConfirmDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.DonDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.DonHandler
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.GameEvents
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.GetPlayerVotes
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.InformatorHandler
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IntegerEvents
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsBomberOnlyAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsCardVotesVisible
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsDoubleDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsExitOrObserveDialogShown
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsMeNotUnderLover
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsPlayerCardClick
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.IsTextFieldPermitted
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.JournalistDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.JournalistHandler
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.LoverDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnBomberAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnBomberVoteOrAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnDetectiveConfirm
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnDetectiveDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnDismissConfirmVoteDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnDoctorAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnDoubleDialogVoteClick
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnExitOrObserveDialogDismiss
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnInformatorConfirm
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnInformatorDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnJournalistConfirm
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnLoverAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnPlayerCardClick
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnSherifAbilityUse
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnTextFieldChange
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.OnVote
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.PlayerCardInteractionViaBarman
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.SherifDialog
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi.UnitEvents
import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.ChatMessage
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



data class BarmanDataState(
    val isBarmanAbilityAvailable: Boolean = false,
    val barmanDialog: Boolean = false,
    val barmanChosenName: String = "",
    val barmanChosenId: Int = -1,
)

data class JournalistDataState(
    val isJournalistAbilityAvailable: Boolean = false,
    val journalistDialog: Boolean = false,
    val journalistChosenName: String = "",
    val firstJournalistChosenId: Int = -1,
    val secondJournalistChosenId: Int = -1,
    val journalistTempId: Int = -1,
)

data class DoctorDataState(
    val isDoctorAbilityAvailable: Boolean = false,
    val doctorDialog: Boolean = false,
    val doctorChosenName: String = "",
    val doctorChosenId: Int = -1,
)
data class SherifDataState(
    val isSherifAbilityAvailable: Boolean = false,
    val sherifDialog: Boolean = false,
    val sherifChosenName: String = "",
    val sherifChosenId: Int = -1,
)

data class DetectiveDataState(
    val isDetectiveAbilityAvailable: Boolean = false,
    val detectiveDialog: Boolean = false,
    val detectiveChosenName: String = "",
    val detectiveChosenId: Int = -1,
)

data class InformatorDataState(
    val isInformatorAbilityAvailable: Boolean = false,
    val informatorDialog: Boolean = false,
    val informatorChosenName: String = "",
    val informatorChosenId: Int = -1,
)

data class LoverDataState(
    val isLoverAbilityAvailable: Boolean = false,
    val loverDialog: Boolean = false,
    val loverChosenName: String = "",
    val loverChosenId: Int = -1,
)

data class BomberDataState(
    val isBomberAbilityAvailable: Boolean = false,
    val bomberDialog: Boolean = false,
    val bomberChosenName: String = "",
    val bomberChosenId: Int = -1,
)

data class DonDataState(
    val isDonAbilityAvailable: Boolean = false,
    val donDialog: Boolean = false,
    val donChosenName: String = "",
    val donChosenId: Int = -1,
)

data class VoteDataState(
    val isVotesReset: Boolean = false,
    val votedPlayerId: Int = -1,
    val isNotVoted: Boolean = false,
    val isConfirmVoteDialog: Boolean = false,
    val votePlayer: String = "",
)

data class CurrentPlayerDataState(
    val playerId: Int = -1,
    val playerName: String = "",
    val message: String = "",
    val isObserving: Boolean = false,
)

class GameRoomViewModel(private val gameRoomServiceHub: GameRoomServiceHub) : ViewModel() {
    val uiState = gameRoomServiceHub.gameRoomState
    val servicePlayersInfo = gameRoomServiceHub.playersInfo

    private var _voteDataState = MutableStateFlow(VoteDataState())
    private var _currentPlayerDataState = MutableStateFlow(CurrentPlayerDataState())

    private var _barmanState = MutableStateFlow(BarmanDataState())
    private var _journalistState = MutableStateFlow(JournalistDataState())
    private var _doctorState = MutableStateFlow(DoctorDataState())
    private var _sherifState = MutableStateFlow(SherifDataState())
    private var _detectiveState = MutableStateFlow(DetectiveDataState())
    private var _informatorState = MutableStateFlow(InformatorDataState())
    private var _loverState = MutableStateFlow(LoverDataState())
    private var _bomberState = MutableStateFlow(BomberDataState())
    private var _donState = MutableStateFlow(DonDataState())

    val voteDataState: StateFlow<VoteDataState> = _voteDataState.asStateFlow()
    val currentPlayerDataState: StateFlow<CurrentPlayerDataState> = _currentPlayerDataState.asStateFlow()

    val barmanState: StateFlow<BarmanDataState> = _barmanState.asStateFlow()
    val journalistState: StateFlow<JournalistDataState> = _journalistState.asStateFlow()
    val doctorState: StateFlow<DoctorDataState> = _doctorState.asStateFlow()
    val sherifState: StateFlow<SherifDataState> = _sherifState.asStateFlow()
    val detectiveState: StateFlow<DetectiveDataState> = _detectiveState.asStateFlow()
    val informatorState: StateFlow<InformatorDataState> = _informatorState.asStateFlow()
    val loverState: StateFlow<LoverDataState> = _loverState.asStateFlow()
    val bomberState: StateFlow<BomberDataState> = _bomberState.asStateFlow()
    val donState: StateFlow<DonDataState> = _donState.asStateFlow()

    enum class DialogState {
        NONE,
        INITIAL_NOTIFICATION,
        ROLE_NOTIFICATION,
        PHASE_NOTIFICATION
    }

    private var pendingPhaseDialog by mutableStateOf(false)

    var currentDialogState by mutableStateOf(DialogState.INITIAL_NOTIFICATION)
    var showPhaseDialog by mutableStateOf(false)

    private fun requestMessage(playerName: String, message: String) = gameRoomServiceHub.requestSendMessage(this._currentPlayerDataState.value.playerId, playerName, message)
    private fun requestJoinGame() = gameRoomServiceHub.requestJoinGame(this._currentPlayerDataState.value.playerId, this._currentPlayerDataState.value.playerName)
    private fun requestRolesQuantity() = gameRoomServiceHub.requestRolesQuantity()
//    private fun requestGameEvent() = gameRoomServiceHub.requestRemitGameEvent()
    private fun requestBarmanAbility(playerId: Int) = gameRoomServiceHub.requestBarmanAbilityUsage(playerId)
    private fun sendJournalistData() = gameRoomServiceHub.sendJournalistData(this._journalistState.value.firstJournalistChosenId, this._journalistState.value.secondJournalistChosenId)
    private fun sendDoctorData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendSherifData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendDetectiveData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendInformatorData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendLoverData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendBomberData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)
    private fun sendDonData(selectedPlayerId: Int) = gameRoomServiceHub.requestPlayerInfo(this._currentPlayerDataState.value.playerId, selectedPlayerId)

    private fun changeVote(gameGuid: String, playerId: Int, phase: String) {
        viewModelScope.launch {
            gameRoomServiceHub.changeVotes(gameGuid = gameGuid, playerId = playerId, phase = phase)
        }
    }

    fun resetPlayerLogicInfo() {
        this._currentPlayerDataState.value =
            this._currentPlayerDataState.value.copy(
                isObserving = false,
                message = ""
            )

        this.gameRoomServiceHub.resetPlayersInfo()
        this.gameRoomServiceHub.resetGameReceiveMessages()
        this.gameRoomServiceHub.resetGameEventMessages()
        this.gameRoomServiceHub.resetGameRoomState()

        this._voteDataState.value = VoteDataState()
        this._donState.value = DonDataState()
        this._barmanState.value = BarmanDataState()
        this._journalistState.value = JournalistDataState()
        this._doctorState.value = DoctorDataState()
        this._sherifState.value = SherifDataState()
        this._detectiveState.value = DetectiveDataState()
        this._informatorState.value = InformatorDataState()
        this._loverState.value = LoverDataState()
        this._bomberState.value = BomberDataState()
    }

    fun setupHub() {
        gameRoomServiceHub.setupGameCalculation(this._currentPlayerDataState.value.playerId)
        gameRoomServiceHub.setupLeaveLogicCalculation(this._currentPlayerDataState.value.playerId)
        gameRoomServiceHub.disconnectDataSend(this._currentPlayerDataState.value.playerId)
        gameRoomServiceHub.setupHubConnection()
    }

    fun startConnection() = gameRoomServiceHub.startConnection()
    fun stopConnection() = gameRoomServiceHub.stopConnection()

    fun requestLeaveGame() = gameRoomServiceHub.requestLeaveGame(this._currentPlayerDataState.value.playerId, this._currentPlayerDataState.value.playerName)
    fun getHubConnection(): HubConnection = gameRoomServiceHub.getHubConnection()

    fun changePlayerName(name: String) {
        this._currentPlayerDataState.value = this._currentPlayerDataState.value.copy(
            playerName = name
        )
    }

    fun changePlayerId(id: Int) {
        this._currentPlayerDataState.value = this._currentPlayerDataState.value.copy(
            playerId = id
        )
    }

    fun onInitialDialogConfirm() {
        currentDialogState = DialogState.ROLE_NOTIFICATION
    }

    fun onRoleDialogConfirm() {
        currentDialogState = if (pendingPhaseDialog) {
            pendingPhaseDialog = false
            showPhaseDialog = true
            DialogState.PHASE_NOTIFICATION
        } else {
            DialogState.NONE
        }
    }

    fun onPhaseDialogConfirm() {
        showPhaseDialog = false
        currentDialogState = DialogState.NONE
    }

    private fun requestPhaseDialog() {
        if (currentDialogState != DialogState.NONE) {
            pendingPhaseDialog = true
        } else {
            showPhaseDialog = true
            currentDialogState = DialogState.PHASE_NOTIFICATION
        }
    }

    private fun initializeVotingForPhase(players: List<Player>) {
        if ( this._voteDataState.value.isVotesReset.not() ) {
            val initialVotes = players.associate { it.playerId to 0 }
            uiState.value.playersToVote = VotePlayers(initialVotes)
            this._voteDataState.value = this._voteDataState.value.copy(
                isVotesReset = true
            )
        }
    }

    private fun resetVotingForNewPhase() {
        this._voteDataState.value = this._voteDataState.value.copy(
            isVotesReset = true,
            isNotVoted = true
        )
        uiState.value.playersToVote = VotePlayers(uiState.value.playersToVote.players.mapValues { 0 })
    }

    private fun onBarmanInfoChange(id: Int, name: String) {
        this._barmanState.value = this._barmanState.value.copy(
            barmanChosenId = id,
            barmanChosenName = name
        )
    }

    fun informatorHelper(selectedPlayerId: Int) = servicePlayersInfo.value.any { p -> p.playerId == selectedPlayerId && p.isUnderInformator }.not()

    fun changePhase(isDay: Boolean) = gameRoomServiceHub.changePhase(isDay)

    fun unitEventHandler(event: UnitEvents) {
        when (event) {
            is DonDialog -> {
                this._donState.value = this._donState.value.copy(
                    donChosenName = event.choseName,
                    donChosenId = event.chosenId,
                    donDialog = event.isDismiss.not()
                )
            }
            is DonConfirmDialog -> {
                sendDonData(event.chosenId)
                this._donState.value = this._donState.value.copy(
                    isDonAbilityAvailable = false,
                    donDialog = false
                )
            }
            is OnDoubleDialogVoteClick -> {
                changeVote(
                    uiState.value.guid,
                    event.chosenId,
                    uiState.value.phase.name
                )
                this._informatorState.value = this._informatorState.value.copy(
                    informatorDialog = false
                )
                this._barmanState.value = this._barmanState.value.copy(
                    barmanDialog = false
                )
                this._voteDataState.value = this._voteDataState.value.copy(
                    isNotVoted = false
                )
            }

            is OnDismissConfirmVoteDialog -> {
                this._voteDataState.value = this._voteDataState.value.copy(
                    isConfirmVoteDialog = false
                )
            }

            is OnVote -> {
                changeVote(
                    uiState.value.guid,
                    event.chosenId,
                    uiState.value.phase.name
                )

                this._voteDataState.value = this._voteDataState.value.copy(
                    isNotVoted = false,
                    isConfirmVoteDialog = false
                )
            }

            is OnExitOrObserveDialogDismiss -> {
                this._currentPlayerDataState.value = this._currentPlayerDataState.value.copy(
                    isObserving = true
                )
//                this._
            }

            is OnPlayerCardClick -> {
                this._voteDataState.value = this._voteDataState.value.copy(
                    votedPlayerId = event.playerId,
                    votePlayer = event.playerName,
                    isConfirmVoteDialog = true
                )
            }

            is BarmanDialog -> {
                this._barmanState.value = this._barmanState.value.copy(
                    barmanDialog = event.isDismiss.not()
                )
            }

            is PlayerCardInteractionViaBarman -> {
                if (this._barmanState.value.barmanDialog) {
                    requestBarmanAbility(event.playerId)
                }

                this._barmanState.value = this._barmanState.value.copy(
                    isBarmanAbilityAvailable = false,
                    barmanDialog = false
                )
            }

            is JournalistDialog -> {
                this._journalistState.value = this._journalistState.value.copy(
                    journalistTempId = event.selectedId,
                    journalistChosenName = event.selectedName + if (this._journalistState.value.firstJournalistChosenId == -1) " (1)" else " (2)",
                    journalistDialog = event.isDismiss.not()
                )
            }

            is OnJournalistConfirm -> {
                if(this._journalistState.value.firstJournalistChosenId == -1) {
                    this._journalistState.value = this._journalistState.value.copy(
                        firstJournalistChosenId = event.playerId
                    )
                } else {
                    this._journalistState.value = this._journalistState.value.copy(
                        secondJournalistChosenId = event.playerId,
                        isJournalistAbilityAvailable = false
                    )
                    sendJournalistData()
                    this._journalistState.value = this._journalistState.value.copy(
                        firstJournalistChosenId = -1,
                        secondJournalistChosenId = -1
                    )
                }

                this._journalistState.value = this._journalistState.value.copy(
                    journalistDialog = false
                )
            }

            is DoctorDialog -> {
                this._doctorState.value = this._doctorState.value.copy(
                    doctorChosenName = event.chosenName,
                    doctorChosenId = event.chosenId,
                    doctorDialog = event.isDismiss.not()
                )
            }

            is SherifDialog -> {
                this._sherifState.value = this._sherifState.value.copy(
                    sherifChosenName = event.chosenName,
                    sherifChosenId = event.chosenId,
                    sherifDialog = event.isDismiss.not()
                )
            }

            is OnDoctorAbilityUse -> {
                sendDoctorData(event.chosenId)
                this._doctorState.value = this._doctorState.value.copy(
                    isDoctorAbilityAvailable = false,
                    doctorDialog = false
                )
            }

            is OnSherifAbilityUse -> {
                sendSherifData(event.chosenId)
                this._sherifState.value = this._sherifState.value.copy(
                    isSherifAbilityAvailable = false,
                    sherifDialog = false
                )
            }

            is OnDetectiveConfirm -> {
                sendDetectiveData(event.chosenId)
                this._detectiveState.value = this._detectiveState.value.copy(
                    isDetectiveAbilityAvailable = false,
                    detectiveDialog = false
                )
            }

            is OnDetectiveDialog -> {
                this._detectiveState.value = this._detectiveState.value.copy(
                    detectiveChosenName = event.chosenName,
                    detectiveChosenId = event.chosenId,
                    detectiveDialog = event.isDismiss.not()
                )
            }

            is OnInformatorConfirm -> {
                sendInformatorData(event.chosenId)
                this._informatorState.value = this._informatorState.value.copy(
                    isInformatorAbilityAvailable = false,
                    informatorDialog = false
                )
            }

            is OnInformatorDialog -> {
                this._informatorState.value = this._informatorState.value.copy(
                    informatorChosenName = event.chosenName,
                    informatorChosenId = event.chosenId,
                    informatorDialog = event.isDismiss.not()
                )
            }

            is OnLoverAbilityUse -> {
                sendLoverData(event.chosenId)
                this._loverState.value = this._loverState.value.copy(
                    loverDialog = false,
                    isLoverAbilityAvailable = false
                )
            }

            is LoverDialog -> {
                this._loverState.value = this._loverState.value.copy(
                    loverChosenId = event.chosenId,
                    loverChosenName = event.chosenName,
                    loverDialog = event.isDismiss.not()
                )
            }

            is BomberDialog -> {
                this._bomberState.value = this._bomberState.value.copy(
                    bomberChosenName = event.chosenName,
                    bomberChosenId = event.chosenId,
                    bomberDialog = event.isDismiss.not()
                )
            }

            is OnBomberAbilityUse -> {
                sendBomberData(event.chosenId)
                this._bomberState.value = this._bomberState.value.copy(
                    isBomberAbilityAvailable = false,
                    bomberDialog = false
                )
            }

            is OnBomberVoteOrAbilityUse -> {
                when(event.chosenAction) {
                    "ability" -> {
                        unitEventHandler(OnBomberAbilityUse(event.chosenId))
                    }

                    "vote" -> {
                        Log.d("isNotVotedCheck", "isNotVoted = ${this._voteDataState.value.isNotVoted}")
                        Log.d("bomberDialog", "bomberDialog = ${this._bomberState.value.bomberDialog}")
                        unitEventHandler(OnVote(event.chosenId))
                        this._bomberState.value = this._bomberState.value.copy(
                            bomberDialog = false
                        )
                    }
                }
            }

            is OnTextFieldChange -> {
                this._currentPlayerDataState.value = this._currentPlayerDataState.value.copy(
                    message = event.text
                )
            }
        }
    }
    fun areNoAliveMafias(selectedPlayerId: Int): Boolean {
        return !uiState.value.alivePlayers.any { alive ->
            uiState.value.oldPlayers.any { old ->
                old.playerId == alive.playerId && old.playerRole is Role.Mafia || old.playerRole is Role.Don || old.playerRole is Role.Mimic
            }
        } && selectedPlayerId != this._currentPlayerDataState.value.playerId
    }

    fun booleanEventHandler(event: BooleanEvents): Boolean {
        val state = uiState.value

        fun isMafiaLike(role: Role) =
            role is Role.Mafia || role is Role.Don || role is Role.Mimic

        fun isSpecialMafiaTown(role: Role) =
            role is Role.Informator || role is Role.Barman || role is Role.Bomber

        fun areNoAliveMafias(): Boolean {
            return !state.alivePlayers.any { alive ->
                state.oldPlayers.any { old ->
                    old.playerId == alive.playerId && isMafiaLike(old.playerRole!!)
                }
            }
        }

        return when (event) {
            is IsDoubleDialog -> {
                if(event.selectedPlayerId == this._currentPlayerDataState.value.playerId && state.alivePlayers.any { alive ->
                        state.oldPlayers.any { old ->
                            old.playerId == alive.playerId && (old.playerRole is Role.Doctor || old.playerRole is Role.Journalist || old.playerRole is Role.Barman).not()
                        }
                    })
                    false
                else
                    state.phase == Phase.NightVote && areNoAliveMafias() && this._currentPlayerDataState.value.isObserving.not()
            }

            is DonHandler -> {
                event.currentPlayer.playerRole is Role.Don && state.phase == Phase.NightDiscussion
                        && this._currentPlayerDataState.value.isObserving.not() &&
                        event.isDead.not() &&
                        event.selectedPlayerId != this._currentPlayerDataState.value.playerId &&
                        this._donState.value.isDonAbilityAvailable &&
                        this._donState.value.donDialog.not() &&
                        servicePlayersInfo.value.any { p -> p.playerId == this._currentPlayerDataState.value.playerId && p.isDonSearch }.not()
            }

            is IsMeNotUnderLover -> {
                servicePlayersInfo.value.any { p ->
                    p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover
                }.not()
            }

            is IsPlayerCardClick -> {
                val notObserving = this._currentPlayerDataState.value.isObserving.not()
                val isNotSamePlayer = event.currentPlayerId != event.playerId
                val notVoted = this._voteDataState.value.isNotVoted
                val notDead = !event.isDead

                val isDayVote =
                    state.phase == Phase.DayVote &&
                            notObserving && isNotSamePlayer && notVoted && notDead

                val isNightVoteMafia =
                    state.phase == Phase.NightVote &&
                            notObserving && isNotSamePlayer && notVoted && notDead &&
                            isMafiaLike(event.currentPlayerRole)

                val isDoctorAbilityClick =
                    state.phase == Phase.NightVote &&
                            event.currentPlayerRole is Role.Doctor &&
                            notObserving &&
                            notDead &&
                            _doctorState.value.doctorDialog.not()

                val isSherifAbilityUse =
                    (state.phase == Phase.NightVote || state.phase == Phase.DayDiscussion) &&
                            event.currentPlayerRole is Role.Sherif &&
                            notObserving &&
                            isNotSamePlayer &&
                            notDead &&
                            _sherifState.value.sherifDialog.not()

                val isBomberAbilityUse =
                    (state.phase == Phase.NightVote || state.phase == Phase.DayDiscussion || state.phase == Phase.DayVote) &&
                            event.currentPlayerRole is Role.Bomber &&
                            notObserving &&
                            isNotSamePlayer &&
                            notDead &&
                            _bomberState.value.bomberDialog.not()

                val isMeLover =
                    state.oldPlayers.any { old ->
                        state.alivePlayers.any { alive ->
                            alive.playerId == this._currentPlayerDataState.value.playerId &&
                                old.playerId == alive.playerId &&
                                old.playerRole == Role.Lover
                        }
                    }

                val isLoverClick = isMeLover &&
                        this._currentPlayerDataState.value.playerId != event.playerId &&
                        uiState.value.phase == Phase.NightDiscussion &&
                        this._loverState.value.isLoverAbilityAvailable &&
                        _loverState.value.loverDialog.not()



                Log.d("DoctorCheck", "Doctor info notDead-> ${notDead} notObserving ${notObserving} isNotSamePlayer ${isNotSamePlayer} event.currentPlayerRole ${event.currentPlayerRole}")

                ((isDayVote ||
                    isNightVoteMafia ||
                    isDoctorAbilityClick ||
                    isSherifAbilityUse ||
                    isBomberAbilityUse ||
                    isLoverClick) && servicePlayersInfo.value.any { p -> p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover }.not())
            }



            is IsExitOrObserveDialogShown -> {
                this._currentPlayerDataState.value.isObserving.not() &&
                event.currentPlayerId == event.playerId
            }

            is IsCardVotesVisible -> {
                val hasVotes = event.votes != 0
                val isDayVote = state.phase == Phase.DayVote
                val isNightVote = state.phase == Phase.NightVote

                val mafiaVisible =
                    isMafiaLike(event.playerRole)

                val specialVisible =
                    isSpecialMafiaTown(event.playerRole) && areNoAliveMafias()

                hasVotes && (isDayVote || (isNightVote && (mafiaVisible || specialVisible)))
            }

            is BarmanHandler -> {
                state.phase == Phase.NightVote && event.currentPlayer.playerRole is Role.Barman && servicePlayersInfo.value.any { p ->
                    p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover }.not() && _currentPlayerDataState.value.isObserving.not()

            }

            is JournalistHandler -> {
                val isNightVote = uiState.value.phase == Phase.NightVote
                val hasAbility = this._journalistState.value.isJournalistAbilityAvailable
                val abilityAvailable = servicePlayersInfo.value.any { p ->
                    p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover
                }.not()
                val isNotUnderJournalistPlayer = event.selectedPlayerId != this._journalistState.value.firstJournalistChosenId &&
                        servicePlayersInfo.value.any { p -> p.playerId == event.selectedPlayerId && p.isUnderJournalist}.not() &&
                        _currentPlayerDataState.value.isObserving.not()

                Log.d("JournalistClickCheck", "isNightVote -> ${isNightVote}")
                Log.d("JournalistClickCheck", "isNightVote -> ${isNotUnderJournalistPlayer}")
                Log.d("JournalistClickCheck", "isNightVote -> ${servicePlayersInfo.value.find { p -> p.playerId == event.selectedPlayerId }?.isUnderJournalist?.not()}")
                Log.d("JournalistClickCheck", "isNightVote -> ${servicePlayersInfo.value.find { p -> p.playerId == event.selectedPlayerId }?.isUnderJournalist?.not()}")
                    isNightVote && hasAbility && abilityAvailable && isNotUnderJournalistPlayer && event.currentPlayer.playerRole is Role.Journalist
            }

            is DetectiveHandler -> {
                return this._currentPlayerDataState.value.playerId != event.selectedPlayerId && state.phase == Phase.NightVote && this._detectiveState.value.isDetectiveAbilityAvailable && servicePlayersInfo.value.any { p ->
                    p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover
                }.not() && servicePlayersInfo.value.any { p -> p.playerId == event.selectedPlayerId && p.isUnderDetective }.not()
                        && event.currentPlayer.playerRole is Role.Detective && _currentPlayerDataState.value.isObserving.not()
                        && _detectiveState.value.detectiveDialog.not()
            }

            is InformatorHandler -> {
                return this._currentPlayerDataState.value.playerId != event.selectedPlayerId && state.phase == Phase.NightVote && servicePlayersInfo.value.any { p ->
                    p.playerId == this._currentPlayerDataState.value.playerId && p.isUnderLover
                }.not() && servicePlayersInfo.value.any { p -> p.playerId == event.selectedPlayerId && p.isUnderInformator }.not()
                        && event.currentPlayer.playerRole is Role.Informator &&
                        _currentPlayerDataState.value.isObserving.not() &&
                        _informatorState.value.informatorDialog.not()
            }

            is IsBomberOnlyAbilityUse -> {
                Log.d("isNotVoted", "isNotVoted = ${this._voteDataState.value.isNotVoted}")
                Log.d("areNoAliveMafias", "areNoAliveMafias = ${areNoAliveMafias()}")
                Log.d("state.phase", "state.phase = ${state.phase}")
                when (state.phase) {
                    Phase.DayDiscussion -> _currentPlayerDataState.value.isObserving.not()
                    Phase.NightVote -> areNoAliveMafias() && this._voteDataState.value.isNotVoted.not() && _currentPlayerDataState.value.isObserving.not()
                    Phase.DayVote -> this._voteDataState.value.isNotVoted.not() && _currentPlayerDataState.value.isObserving.not()
                    else -> _currentPlayerDataState.value.isObserving.not()
                }
            }

            is IsTextFieldPermitted -> when (uiState.value.phase) {
                Phase.NightDiscussion -> {
                    event.currentPlayer != null && uiState.value.alivePlayers.any { p -> p.playerId == event.currentPlayer.playerId }
                            && (
                            event.currentPlayer.playerRole is Role.Mafia ||
                                    event.currentPlayer.playerRole is Role.Don ||
                                    event.currentPlayer.playerRole is Role.Informator ||
                                    event.currentPlayer.playerRole is Role.Mimic
                            ) && _currentPlayerDataState.value.isObserving.not()
                }

                Phase.DayDiscussion -> {
                    event.currentPlayer != null && uiState.value.alivePlayers.any { p -> p.playerId == event.currentPlayer.playerId }
                    && _currentPlayerDataState.value.isObserving.not()
                }

                else -> false
            }
        }
    }

    fun integerEventHandler(event: IntegerEvents): Int {
        return when( event ) {
            is GetPlayerVotes -> uiState.value.playersToVote.players[event.id]!!
        }
    }

    fun sendMessagePressed(currentPlayer: Player?) {
        if (this._currentPlayerDataState.value.message.isNotBlank()) {
            val canSend = when (uiState.value.phase) {
                Phase.NightDiscussion -> {
                    currentPlayer != null && uiState.value.alivePlayers.any{ p -> p.playerId == currentPlayer.playerId } && (
                            currentPlayer.playerRole is Role.Mafia ||
                                    currentPlayer.playerRole is Role.Don ||
                                    currentPlayer.playerRole is Role.Informator ||
                                    currentPlayer.playerRole is Role.Mimic
                            )
                }
                Phase.DayDiscussion -> {
                    currentPlayer != null && uiState.value.alivePlayers.any{ p -> p.playerId == currentPlayer.playerId }
                }
                else -> false
            }

            if (canSend) {
                viewModelScope.launch {
                    val senderName = if (uiState.value.phase == Phase.NightDiscussion &&
                        currentPlayer?.playerRole is Role.Informator
                    ) {
                        "Informator"
                    } else {
                        _currentPlayerDataState.value.playerName
                    }
                    requestMessage(senderName, _currentPlayerDataState.value.message)
                    _currentPlayerDataState.value = _currentPlayerDataState.value.copy(
                        message = ""
                    )
                }
            }
        }
    }

    fun backgroundSnapshot(snapshotPhase: Phase, role: Role, currentPlayer: Player?, player: Player): Color {
        return when {
            currentPlayer?.playerRole == null -> Color.Yellow
            currentPlayer.playerRole is Role.Mimic -> {
                when (role) {
                    is Role.Informator -> if (snapshotPhase == Phase.NightDiscussion) Color(0xFFFF4444) else Color(0xFF4CAF50)
                    is Role.Don, is Role.Mafia -> Color(0xFFFF4444)
                    else -> Color(0xFF4CAF50)
                }
            }
            currentPlayer.playerRole is Role.Informator -> {
                if (snapshotPhase == Phase.NightDiscussion && (role is Role.Don || role is Role.Mafia || role is Role.Mimic || role is Role.Informator))
                    Color(0xFFFF4444)
                else if (player.playerRole != null && snapshotPhase == Phase.DayDiscussion && servicePlayersInfo.value.any { p -> p.playerId == player.playerId && p.isUnderInformator})
                    when (player.playerRole) {
                        is Role.Don, is Role.Mafia -> Color(0xFFFF4444)
                        is Role.Barman, is Role.Bomber -> Color(0xFFFF4444)
                        else -> Color(0xFF4CAF50)
                    }
                else
                    Color(0xFF4CAF50)
            }

            currentPlayer.playerRole is Role.Don || currentPlayer.playerRole is Role.Mafia -> {
                when (role) {
                    is Role.Informator -> if (snapshotPhase == Phase.NightDiscussion) Color(0xFFFF4444) else Color(0xFF4CAF50)
                    is Role.Don, is Role.Mafia, is Role.Mimic -> Color(0xFFFF4444)
                    else -> Color(0xFF4CAF50)
                }
            }

            currentPlayer.playerRole is Role.Detective -> {
                if(player.playerRole != null && snapshotPhase == Phase.DayDiscussion && servicePlayersInfo.value.any { p -> p.playerId == player.playerId && p.isUnderDetective})
                    when (player.playerRole) {
                        is Role.Don, is Role.Mafia, is Role.Mimic -> Color(0xFFFF4444)
                        is Role.Informator, is Role.Barman, is Role.Bomber -> Color(0xFFFF4444)
                        else -> Color(0xFF4CAF50)
                    }
                else
                    Color(0xFF4CAF50)
            }
            else -> Color(0xFF4CAF50)
        }
    }

    fun allMessages(currentPlayer: Player?): List<ChatMessage> {
        return if (currentPlayer != null &&
            (currentPlayer.playerRole is Role.Mafia ||
                currentPlayer.playerRole is Role.Don ||
                currentPlayer.playerRole is Role.Informator ||
                currentPlayer.playerRole is Role.Mimic)
        ) {
            uiState.value.mafiaMessages + uiState.value.messages
        } else {
            uiState.value.messages
        }.sortedBy { it.timeStamp }
    }

    fun resetInitialNotification() {
        this.pendingPhaseDialog = false
        this.currentDialogState = DialogState.INITIAL_NOTIFICATION
        this.showPhaseDialog = false
    }

    fun gameRoomEventsHandler(handler: GameEvents): String {
        return when(handler) {
            is GameEvents.SnapshotTextFirstLetter -> {
                when (handler.snapshotPhase) {
                    Phase.NightDiscussion -> {
                        when {
                            handler.currentPlayer?.playerRole is Role.Informator &&
                                    (handler.itPlayer.playerRole is Role.Don ||
                                            handler.itPlayer.playerRole is Role.Mafia ||
                                            handler.itPlayer.playerRole is Role.Mimic) ->
                                "Mafia".first().uppercaseChar().toString()
                            handler.itPlayer.playerRole is Role.Informator ->
                                "Informator".first().uppercaseChar().toString()
                            else ->
                                handler.itPlayer.playerName.firstOrNull()?.uppercase() ?: "?"
                        }
                    }
                    else ->
                        handler.itPlayer.playerName.firstOrNull()?.uppercase() ?: "?"
                }
            }

            is GameEvents.SnapshotTextName -> {
                when (handler.snapshotPhase) {
                    Phase.NightDiscussion -> {
                        when {
                            handler.currentPlayer?.playerRole is Role.Informator &&
                                    (handler.itPlayer.playerRole is Role.Don ||
                                            handler.itPlayer.playerRole is Role.Mafia ||
                                            handler.itPlayer.playerRole is Role.Mimic) ->
                                "Mafia"
                            handler.itPlayer.playerRole is Role.Informator ->
                                "Informator"
                            else ->
                                handler.itPlayer.playerName
                        }
                    }
                    else ->
                        handler.itPlayer.playerName
                }
            }
        }
    }

    fun onCardClick(currentPlayer: Player, player: Player) {
        if (uiState.value.phase == Phase.NightVote) {
            when (currentPlayer.playerRole) {
                is Role.Mafia, is Role.Don, is Role.Mimic -> unitEventHandler(
                    OnPlayerCardClick(player.playerId, player.playerName)
                )

                is Role.Barman -> {
                    onBarmanInfoChange(player.playerId, player.playerName)
                    unitEventHandler(BarmanDialog(false))
                }

                is Role.Journalist -> {
                    unitEventHandler(
                        JournalistDialog(
                            isDismiss = false,
                            player.playerName,
                            player.playerId
                        )
                    )
                }

                is Role.Bomber -> {
                    unitEventHandler(
                        BomberDialog(
                            isDismiss = false,
                            chosenId = player.playerId,
                            chosenName = player.playerName
                        )
                    )
                }

                is Role.Detective -> {
                    unitEventHandler(
                        OnDetectiveDialog(
                            isDismiss = false,
                            chosenName = player.playerName,
                            chosenId = player.playerId
                        )
                    )
                }

                is Role.Informator -> {
                    unitEventHandler(
                        OnInformatorDialog(
                            isDismiss = false,
                            chosenName = player.playerName,
                            chosenId = player.playerId
                        )
                    )
                }

                is Role.Doctor -> {
                    unitEventHandler(
                        DoctorDialog(
                            isDismiss = false,
                            chosenName = player.playerName,
                            chosenId = player.playerId
                        )
                    )
                    Log.d("DoctorCheck", "Doctor dialog entered")
                }

                is Role.Sherif -> {
                    unitEventHandler(
                        SherifDialog(
                            isDismiss = false,
                            chosenId = player.playerId,
                            chosenName = player.playerName
                        )
                    )
                }

                is Role.Civilian -> {}

                is Role.Lover -> {}

                null -> throw Exception("Player role is null")
            }
        } else if (uiState.value.phase == Phase.DayDiscussion) {
            when (currentPlayer.playerRole) {
                is Role.Sherif -> {
                    unitEventHandler(
                        SherifDialog(
                            isDismiss = false,
                            chosenId = player.playerId,
                            chosenName = player.playerName
                        )
                    )
                }

                is Role.Bomber -> {
                    unitEventHandler(
                        BomberDialog(
                            isDismiss = false,
                            chosenId = player.playerId,
                            chosenName = player.playerName
                        )
                    )
                }

                else -> {}
            }
        } else if (uiState.value.phase == Phase.DayVote) {
            when (currentPlayer.playerRole) {
                is Role.Bomber -> {
                    unitEventHandler(
                        BomberDialog(
                            isDismiss = false,
                            chosenId = player.playerId,
                            chosenName = player.playerName
                        )
                    )
                }

                else -> {
                    unitEventHandler(
                        OnPlayerCardClick(
                            playerId = player.playerId,
                            playerName = player.playerName
                        )
                    )
                }
            }
        } else {
            unitEventHandler(
                DonDialog(
                    isDismiss = false,
                    player.playerName,
                    player.playerId
                )
            )

            unitEventHandler(
                LoverDialog(
                    isDismiss = false,
                    chosenId = player.playerId,
                    chosenName = player.playerName
                )
            )
        }
    }

    fun isClickable(player: Player, currentPlayer: Player, isPlayerDead: Boolean): Boolean {
        return booleanEventHandler(IsMeNotUnderLover) &&
            (booleanEventHandler(
                IsPlayerCardClick(
                    currentPlayerId = currentPlayer.playerId,
                    playerId = player.playerId,
                    isDead = isPlayerDead,
                    currentPlayerRole = currentPlayer.playerRole,
                    playerRole = player.playerRole
                )
            ) || booleanEventHandler(
                BarmanHandler(
                    currentPlayer
                )
            ) || booleanEventHandler(
                JournalistHandler(
                    selectedPlayerId = player.playerId,
                    currentPlayer = currentPlayer
                )
            ) || booleanEventHandler(
                DetectiveHandler(
                    selectedPlayerId = player.playerId,
                    currentPlayer = currentPlayer
                )
            ) || booleanEventHandler(
                InformatorHandler(
                    selectedPlayerId = player.playerId,
                    currentPlayer = currentPlayer
                )
            ) || booleanEventHandler(
                IsDoubleDialog(
                    player.playerId
                )
            )
        ) || booleanEventHandler(
            DonHandler(
                player.playerId,
                currentPlayer,
                isPlayerDead
            )
        )
    }

    fun onPhaseChanges() {
        if (uiState.value.phase == Phase.NightDiscussion || uiState.value.phase == Phase.DayDiscussion) {
            requestPhaseDialog()
            initializeVotingForPhase(uiState.value.oldPlayers)
            this._voteDataState.value = this._voteDataState.value.copy(
                isConfirmVoteDialog = false,
                isNotVoted = false
            )
        } else if(uiState.value.phase == Phase.NightVote || uiState.value.phase == Phase.DayVote) {
            resetVotingForNewPhase()
        }

        if (uiState.value.phase == Phase.NightVote) {
            this._barmanState.value = this._barmanState.value.copy(
                isBarmanAbilityAvailable = true
            )

            this._doctorState.value = this._doctorState.value.copy(
                isDoctorAbilityAvailable = true
            )

            this._sherifState.value = this._sherifState.value.copy(
                isSherifAbilityAvailable = true
            )

            this._journalistState.value = this._journalistState.value.copy(
                isJournalistAbilityAvailable = true
            )

            this._detectiveState.value = this._detectiveState.value.copy(
                isDetectiveAbilityAvailable = true
            )

            this._informatorState.value = this._informatorState.value.copy(
                isInformatorAbilityAvailable = true
            )

            this._bomberState.value = this._bomberState.value.copy(
                isBomberAbilityAvailable = true
            )

            this._loverState.value = this._loverState.value.copy(
                isLoverAbilityAvailable = false
            )

            this._donState.value = this._donState.value.copy(
                donDialog = false
            )
        }

        if (uiState.value.phase == Phase.DayDiscussion) {
            this._barmanState.value = this._barmanState.value.copy(
                isBarmanAbilityAvailable = false,
                barmanDialog = false
            )

            this._doctorState.value = this._doctorState.value.copy(
                isDoctorAbilityAvailable = false,
                doctorDialog = false
            )

            this._journalistState.value = this._journalistState.value.copy(
                isJournalistAbilityAvailable = false,
                journalistDialog = false
            )

            this._detectiveState.value = this._detectiveState.value.copy(
                isDetectiveAbilityAvailable = false,
                detectiveDialog = false
            )

            this._informatorState.value = this._informatorState.value.copy(
                isInformatorAbilityAvailable = false,
                informatorDialog = false
            )
        }

        if (uiState.value.phase == Phase.NightDiscussion) {
            this._bomberState.value = this._bomberState.value.copy(
                isBomberAbilityAvailable = false,
                bomberDialog = false
            )

            this._loverState.value = this._loverState.value.copy(
                isLoverAbilityAvailable = true
            )

            this._donState.value = this._donState.value.copy(
                isDonAbilityAvailable = true
            )
        }

        if( uiState.value.phase == Phase.DayVote ) {
            this._sherifState.value = this._sherifState.value.copy(
                sherifDialog = false,
                isSherifAbilityAvailable = false
            )
        }
    }

    fun sendPhase() = gameRoomServiceHub.requestPhase()

    suspend fun getGameInitialRequest() {
        if (getHubConnection().connectionState == HubConnectionState.CONNECTED) {
            Log.d("MafiaGameRoom", "CONNECTED → send Join & initial requests")
            requestJoinGame()
            delay(500)
            requestRolesQuantity()
//            requestGameEvent()
        }
    }
}
