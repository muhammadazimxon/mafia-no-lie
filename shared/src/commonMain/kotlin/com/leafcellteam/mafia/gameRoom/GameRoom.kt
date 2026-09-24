package com.leafcellteam.mafia.gameRoom

import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import com.leafcellteam.mafia.platform.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.gameRoom.components.BackRequestDialog
import com.leafcellteam.mafia.gameRoom.components.ChatMessageItem
import com.leafcellteam.mafia.gameRoom.components.GameInfo
import com.leafcellteam.mafia.gameRoom.components.dialogs.ConfirmVoteDialog
import com.leafcellteam.mafia.gameRoom.components.dialogs.CustomDialog
import com.leafcellteam.mafia.gameRoom.components.dialogs.ExitOrObserveDialog
import com.leafcellteam.mafia.gameRoom.components.dialogs.base.AbilityOrVoteDialog
import com.leafcellteam.mafia.gameRoom.components.playerCard.PlayerCard
import com.leafcellteam.mafia.gameRoom.components.playerCard.PlayerCardParams
import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
import com.leafcellteam.mafia.gameRoom.models.ChatMessageItemParams
import com.leafcellteam.mafia.gameRoom.models.Phase
import com.leafcellteam.mafia.gameRoom.mvi.BarmanDialog
import com.leafcellteam.mafia.gameRoom.mvi.BomberDialog
import com.leafcellteam.mafia.gameRoom.mvi.DoctorDialog
import com.leafcellteam.mafia.gameRoom.mvi.DonConfirmDialog
import com.leafcellteam.mafia.gameRoom.mvi.DonDialog
import com.leafcellteam.mafia.gameRoom.mvi.GameEvents
import com.leafcellteam.mafia.gameRoom.mvi.GetPlayerVotes
import com.leafcellteam.mafia.gameRoom.mvi.IsBomberOnlyAbilityUse
import com.leafcellteam.mafia.gameRoom.mvi.IsCardVotesVisible
import com.leafcellteam.mafia.gameRoom.mvi.IsExitOrObserveDialogShown
import com.leafcellteam.mafia.gameRoom.mvi.IsTextFieldPermitted
import com.leafcellteam.mafia.gameRoom.mvi.JournalistDialog
import com.leafcellteam.mafia.gameRoom.mvi.LoverDialog
import com.leafcellteam.mafia.gameRoom.mvi.OnBomberVoteOrAbilityUse
import com.leafcellteam.mafia.gameRoom.mvi.OnDetectiveConfirm
import com.leafcellteam.mafia.gameRoom.mvi.OnDetectiveDialog
import com.leafcellteam.mafia.gameRoom.mvi.OnDoctorAbilityUse
import com.leafcellteam.mafia.gameRoom.mvi.OnDoubleDialogVoteClick
import com.leafcellteam.mafia.gameRoom.mvi.OnExitOrObserveDialogDismiss
import com.leafcellteam.mafia.gameRoom.mvi.OnInformatorConfirm
import com.leafcellteam.mafia.gameRoom.mvi.OnInformatorDialog
import com.leafcellteam.mafia.gameRoom.mvi.OnJournalistConfirm
import com.leafcellteam.mafia.gameRoom.mvi.OnLoverAbilityUse
import com.leafcellteam.mafia.gameRoom.mvi.OnPlayerCardClick
import com.leafcellteam.mafia.gameRoom.mvi.OnSherifAbilityUse
import com.leafcellteam.mafia.gameRoom.mvi.OnTextFieldChange
import com.leafcellteam.mafia.gameRoom.mvi.PlayerCardInteractionViaBarman
import com.leafcellteam.mafia.gameRoom.mvi.SherifDialog
import com.leafcellteam.mafia.roles.Role
import com.leafcellteam.mafia.waitingSection.components.ConnectingToServerScreen
import com.leafcellteam.mafia.network.HubState
import com.leafcellteam.mafia.resources.badInternetConnection
import com.leafcellteam.mafia.resources.connectionFailed
import com.leafcellteam.mafia.resources.loading
import com.leafcellteam.mafia.resources.ok
import com.leafcellteam.mafia.resources.players
import com.leafcellteam.mafia.resources.discussion
import com.leafcellteam.mafia.resources.enterMessage
import com.leafcellteam.mafia.resources.wait
import com.leafcellteam.mafia.resources.sec_lowerCase
import com.leafcellteam.mafia.resources.notification
import com.leafcellteam.mafia.resources.welcomeToTheGame
import com.leafcellteam.mafia.resources.yourRole
import com.leafcellteam.mafia.resources.youAre
import com.leafcellteam.mafia.resources.nightBegins
import com.leafcellteam.mafia.resources.theCitySleeps
import com.leafcellteam.mafia.resources.daylightBreaks
import com.leafcellteam.mafia.resources.theSunRises
import com.leafcellteam.mafia.resources.ability
import com.leafcellteam.mafia.resources.moon
import com.leafcellteam.mafia.resources.sun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Clock

@Composable
fun MafiaGameRoom(
    modifier: Modifier,
    gameRoomViewModel: GameRoomViewModel,
    onBack: () -> Unit
) {
    logd("MafiaGameRoom", "Composable initialization started")

    DisposableEffect(gameRoomViewModel.getHubConnection()) {
        logd("MafiaGameRoom", "DisposableEffect: Setting up event handlers")
        gameRoomViewModel.setupHub()
        onDispose {
            logd("MafiaGameRoom", "DisposableEffect: Disposing and stopping connection")
            gameRoomViewModel.stopConnection()
        }
    }

    LaunchedEffect(Unit) {
        logd("MafiaGameRoom", "Initializing hub")
        gameRoomViewModel.startConnection()
    }

    val ability = stringResource(Res.string.ability)
    val uiState by gameRoomViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val voteDataState by gameRoomViewModel.voteDataState.collectAsState()
    val currentPlayerDataState by gameRoomViewModel.currentPlayerDataState.collectAsState()

    var connectionSuccessful by remember { mutableStateOf(false) }
    var timedOut by remember { mutableStateOf(false) }
    var showConnectionFailedDialog by remember { mutableStateOf(false) }

    val barmanDataState by gameRoomViewModel.barmanState.collectAsState()
    val journalistDataState by gameRoomViewModel.journalistState.collectAsState()
    val doctorDataState by gameRoomViewModel.doctorState.collectAsState()
    val sherifDataState by gameRoomViewModel.sherifState.collectAsState()
    val detectiveDataState by gameRoomViewModel.detectiveState.collectAsState()
    val informatorDataState by gameRoomViewModel.informatorState.collectAsState()
    val loverDataState by gameRoomViewModel.loverState.collectAsState()
    val bomberDataState by gameRoomViewModel.bomberState.collectAsState()
    val donDataState by gameRoomViewModel.donState.collectAsState()

    var lastSendTime by remember { mutableLongStateOf(0L) }
    var canSend by remember { mutableStateOf(true) }
    var remainingTime by remember { mutableIntStateOf(0) }

    LaunchedEffect(lastSendTime) {
        if (lastSendTime > 0) {
            canSend = false
            for (i in 6 downTo 1) {
                remainingTime = i
                delay(1000L)
            }
            canSend = true
            remainingTime = 0
        }
    }

    val remainingChars = 200 - currentPlayerDataState.message.length
    val showCharacterCount = currentPlayerDataState.message.length > 150
    val currentLength = currentPlayerDataState.message.length
    val maxLength = 200

    LaunchedEffect(Unit) {
        launch {
            delay(35_000)
            if (!connectionSuccessful) {
                loge("MafiaGameRoom", "❌ Connection timeout after 35 seconds - showing failure dialog and returning")
                timedOut = true
                try {
                    gameRoomViewModel.stopConnection()
                } catch (e: Exception) {
                    loge("MafiaGameRoom", "Error while stopping connection on timeout", e)
                }

                showConnectionFailedDialog = true
                delay(3_000)
                onBack()
            }
        }
    }

    LaunchedEffect(gameRoomViewModel.getHubConnection().state) {
        var attempts = 0
        val maxAttempts = 35

        while (attempts < maxAttempts && !timedOut) {
            val state = gameRoomViewModel.getHubConnection().state
            logd("MafiaGameRoom", "Connection state: $state (attempt $attempts)")

            if (state == HubState.CONNECTED) {
                logd("MafiaGameRoom", "✅ Connected! Waiting 500ms before requests...")
                delay(500)

                logd("MafiaGameRoom", "Sending initial requests")
                gameRoomViewModel.getGameInitialRequest()

                delay(2000)

                if (uiState.oldPlayers.isNotEmpty()) {
                    connectionSuccessful = true
                    logd("MafiaGameRoom", "✅ Players data received successfully")
                }
                break
            }

            delay(100)
            attempts++
        }

        if (attempts >= maxAttempts) {
            loge("MafiaGameRoom", "❌ Failed to connect after ${maxAttempts * 100}ms")
        }
    }

    LaunchedEffect(uiState.oldPlayers) {
        if (gameRoomViewModel.getHubConnection().state == HubState.CONNECTED) {
            if (uiState.oldPlayers.isNotEmpty()) {
                connectionSuccessful = true
                logd("MafiaGameRoom", "✅ Players data loaded successfully")
            }
        }
    }

    val hubState = gameRoomViewModel.getHubConnection().state
    val isLoading = uiState.oldPlayers.isEmpty() && hubState != HubState.CONNECTED && !timedOut

    if (isLoading) {
        ConnectingToServerScreen(stringResource(Res.string.loading) + "...")
    }

    if (showConnectionFailedDialog) {
        CustomDialog(
            title = stringResource(Res.string.connectionFailed),
            text = stringResource(Res.string.badInternetConnection),
            onConfirm = { },
            confirmText = stringResource(Res.string.ok),
            roleImage = null
        )

        LaunchedEffect(true) {
            delay(3000)
            onBack()
            gameRoomViewModel.stopConnection()
        }
    }

    LaunchedEffect(uiState.phase) {
        gameRoomViewModel.onPhaseChanges()
    }

    var isBackRequestDialogOn by remember {
        mutableStateOf(false).also {
            logd("MafiaGameRoom", "Back dialog state initialized: ${it.value}")
        }
    }
    var initialNotificationHelper by remember {
        mutableStateOf(true)
    }

    val currentPlayer = uiState.oldPlayers.find { player ->
        logd("MAFIA_GAME_ROOM_PLAYERS", "MafiaGameRoom: player: ${player.playerId}, ${player.playerName}")
        player.playerId == currentPlayerDataState.playerId && player.playerName == currentPlayerDataState.playerName
    }

    val messagesToShow = gameRoomViewModel.allMessages(currentPlayer = currentPlayer)

    LaunchedEffect(messagesToShow.size) {
        if (messagesToShow.isNotEmpty()) {
            listState.animateScrollToItem(messagesToShow.lastIndex)
        }
    }

    if (isBackRequestDialogOn) {
        BackRequestDialog(
            onDismissRequest = {
                isBackRequestDialogOn = false
            },
            onBack = onBack
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .background(Color(0xFF1E1E2E))
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "${stringResource(Res.string.players)}: ${uiState.oldPlayers.size}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.oldPlayers) { player ->
                        val isPlayerDead = uiState.alivePlayers.any { alivePlayer -> player.playerId == alivePlayer.playerId }.not()
                        val isExitOrObserveDialogShown = gameRoomViewModel.booleanEventHandler(
                            IsExitOrObserveDialogShown(
                                currentPlayerId = currentPlayer?.playerId ?: -1,
                                playerId = player.playerId,
                                isDead = isPlayerDead
                            )
                        )

                        PlayerCard(
                            PlayerCardParams(
                                player = player,
                                currentPlayer = currentPlayer,
                                isDead = isPlayerDead,
                                isKnownForPlayer = gameRoomViewModel.servicePlayersInfo.collectAsState().value.any { p -> p.playerId == player.playerId && (p.isUnderDetective || p.isUnderInformator || p.isDonSearch) },
                                getPlayerVotes = gameRoomViewModel.integerEventHandler(GetPlayerVotes(player.playerId)),
                                isClickable = gameRoomViewModel.isClickable(player, currentPlayer!!, isPlayerDead),
                                isCardVotesVisible = gameRoomViewModel.booleanEventHandler(
                                    IsCardVotesVisible(
                                        votes = gameRoomViewModel.integerEventHandler(
                                            GetPlayerVotes(
                                                player.playerId
                                            )
                                        ),
                                        playerRole = currentPlayer!!.playerRole
                                    )
                                ),
                                onCardClick = { gameRoomViewModel.onCardClick(currentPlayer, player) }
                            )
                        )

                        if (isExitOrObserveDialogShown && (isPlayerDead || uiState.isGameEnd)) {
                            ExitOrObserveDialog(
                                onExitClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        if (uiState.isGameEnd) {
                                            delay(2000)
                                        }
                                        onBack()
                                    }
                                },
                                onDismiss = {
                                    gameRoomViewModel.unitEventHandler(
                                        OnExitOrObserveDialogDismiss(
                                            isPlayerDead,
                                            currentPlayer?.playerId ?: -1,
                                            player.playerId
                                        )
                                    )
                                },
                                endGameMessage = uiState.endGameMessage,
                                isGameEnd = uiState.isGameEnd
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF282838))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .padding(8.dp)
            ) {
                GameInfo(
                    eventInfo = uiState.gameEvents,
                    currentPhase = uiState.phase,
                    quantities = listOf(uiState.mafiaQuantity, uiState.civilianQuantity, uiState.totalAlivePlayers),
                    counter = uiState.timer
                )
            }
            Column(
                modifier = Modifier
                    .weight(1.8f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        Color(0xFF20212C),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Text(
                    text = stringResource(Res.string.discussion),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    logd("Check", "currentPlayer -> $currentPlayer")

                    items(messagesToShow) { content ->
                        key(content.timeStamp.toEpochMilliseconds().milliseconds) {
                            ChatMessageItem(
                                ChatMessageItemParams(
                                    message = content.message,
                                    player = uiState.oldPlayers.find { it.playerId == content.playerId }?.copy(playerName = content.user),
                                    phase = content.phaseWhenSent,
                                    timeStamp = content.timeStamp,
                                    onSnapshotTextFirstLetter = { snapshotPhase, itPlayer -> gameRoomViewModel.gameRoomEventsHandler(GameEvents.SnapshotTextFirstLetter(snapshotPhase = snapshotPhase, currentPlayer = currentPlayer, itPlayer = itPlayer)) },
                                    onSnapshotTextName = { snapshotPhase, itPlayer -> gameRoomViewModel.gameRoomEventsHandler(GameEvents.SnapshotTextName(snapshotPhase = snapshotPhase, currentPlayer = currentPlayer, itPlayer = itPlayer)) },
                                    onBackgroundSnapshot = { snapshotPhase, role, snapshotPlayer -> gameRoomViewModel.backgroundSnapshot(snapshotPhase = snapshotPhase, role = role, currentPlayer = currentPlayer, player = snapshotPlayer) }
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        enabled = gameRoomViewModel.booleanEventHandler(IsTextFieldPermitted(currentPlayer)) && canSend,
                        value = currentPlayerDataState.message,
                        onValueChange = { message ->
                            if(message.length <= 200) {
                                gameRoomViewModel.unitEventHandler(OnTextFieldChange(message))
                            }
                        },
                        minLines = 1,
                        maxLines = 1,
                        placeholder = {
                            Text(
                                if (canSend) {
                                    stringResource(Res.string.enterMessage)
                                } else {
                                    "${stringResource(Res.string.wait)} $remainingTime ${stringResource(Res.string.sec_lowerCase)}..."
                                }
                            )
                        },
                        label = if (currentLength > 0) {
                            {
                                Text(
                                    text = "$currentLength/$maxLength",
                                    color = when {
                                        currentLength >= 200 -> Color(0xFFFF6B6B)
                                        currentLength >= 180 -> Color(0xFFFFD93D)
                                        else -> Color(0xFF7B68EE)
                                    }
                                )
                            }
                        } else null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .animateContentSize()
                            .then(
                                if (uiState.phase == Phase.DayVote || uiState.phase == Phase.NightVote) {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = Color(0xFF7B68EE).copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                } else {
                                    Modifier
                                }
                            ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B68EE),
                            unfocusedBorderColor = Color(0xFF5A5A5A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF7B68EE),
                            focusedLabelColor = Color(0xFF7B68EE),
                            unfocusedLabelColor = Color.Gray,

                            disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                            disabledTextColor = Color.Gray.copy(alpha = 0.5f),
                            disabledLabelColor = Color.Gray.copy(alpha = 0.3f),
                            disabledPlaceholderColor = Color.Gray.copy(alpha = 0.3f),

                            focusedContainerColor = Color(0xFF2A2B3D).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFF20212C).copy(alpha = 0.6f),
                            disabledContainerColor = Color(0xFF1A1A1A).copy(alpha = 0.3f)
                        )
                    )

                    IconButton(
                        onClick = {
                            if (canSend && currentPlayerDataState.message.isNotBlank()) {
                                gameRoomViewModel.sendMessagePressed(currentPlayer = currentPlayer)
                                lastSendTime = Clock.System.now().toEpochMilliseconds()
                            }
                        },
                        enabled = canSend && currentPlayerDataState.message.isNotBlank() && gameRoomViewModel.booleanEventHandler(IsTextFieldPermitted(currentPlayer)),
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (canSend && currentPlayerDataState.message.isNotBlank()) Color(0xFF7B68EE) else Color.Gray,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        BackHandler {
            isBackRequestDialogOn = true
        }

        if (initialNotificationHelper) {
            gameRoomViewModel.resetInitialNotification()
            initialNotificationHelper = false
        }

        if (voteDataState.isConfirmVoteDialog) {
            ConfirmVoteDialog(
                playerName = voteDataState.votePlayer,
                unitEventHandler = gameRoomViewModel::unitEventHandler,
                chosenId = voteDataState.votedPlayerId
            )
        }

        if (gameRoomViewModel.currentDialogState == GameRoomViewModel.DialogState.INITIAL_NOTIFICATION) {
            CustomDialog(
                title = stringResource(Res.string.notification),
                text = stringResource(Res.string.welcomeToTheGame),
                onConfirm = { gameRoomViewModel.onInitialDialogConfirm() },
                confirmText = stringResource(Res.string.ok),
                roleImage = null,
            )
        }

        if (gameRoomViewModel.currentDialogState == GameRoomViewModel.DialogState.ROLE_NOTIFICATION && currentPlayer?.playerRole != null) {
            logd("ChangePlayerId", "MafiaGameRoom: ${currentPlayer.playerRole::class.simpleName}")
            CustomDialog(
                title = stringResource(Res.string.yourRole),
                text = "${stringResource(Res.string.youAre)} ${if(currentPlayer.playerRole is Role.Bomber) "Engineer saboteur" else currentPlayer.playerRole::class.simpleName.orEmpty().lowercase().replaceFirstChar { it.uppercase() }}",
                onConfirm = { gameRoomViewModel.onRoleDialogConfirm() },
                confirmText = stringResource(Res.string.ok),
                roleImage = currentPlayer.playerRole.image,
            )
        }

        if (gameRoomViewModel.currentDialogState == GameRoomViewModel.DialogState.PHASE_NOTIFICATION && gameRoomViewModel.showPhaseDialog) {
            when (uiState.phase) {
                Phase.NightDiscussion -> {
                    CustomDialog(
                        title = stringResource(Res.string.nightBegins),
                        text = stringResource(Res.string.theCitySleeps),
                        onConfirm = { gameRoomViewModel.onPhaseDialogConfirm() },
                        confirmText = stringResource(Res.string.ok),
                        roleImage = Res.drawable.moon,
                    )
                }

                Phase.DayDiscussion -> {
                    CustomDialog(
                        title = stringResource(Res.string.daylightBreaks),
                        text = stringResource(Res.string.theSunRises),
                        onConfirm = { gameRoomViewModel.onPhaseDialogConfirm() },
                        confirmText = stringResource(Res.string.ok),
                        roleImage = Res.drawable.sun,
                    )
                }
                else -> {}
            }
        }

        if (donDataState.donDialog && donDataState.isDonAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { _ -> gameRoomViewModel.unitEventHandler(DonConfirmDialog(donDataState.donChosenId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(DonDialog(true, "", -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(DonDialog(true, "", -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = donDataState.donChosenName
            )
        }

        if (barmanDataState.barmanDialog) {
            if (barmanDataState.isBarmanAbilityAvailable && voteDataState.isNotVoted && gameRoomViewModel.areNoAliveMafias(barmanDataState.barmanChosenId)) {
                AbilityOrVoteDialog(
                    onConfirm = { isAbilityOrVote ->

                        if (isAbilityOrVote == ability)
                            gameRoomViewModel.unitEventHandler(PlayerCardInteractionViaBarman(playerId = barmanDataState.barmanChosenId))
                        else
                            gameRoomViewModel.unitEventHandler(OnDoubleDialogVoteClick(barmanDataState.barmanChosenId))
                    },
                    onCancel = { gameRoomViewModel.unitEventHandler(BarmanDialog(true)) },
                    onDismiss = { gameRoomViewModel.unitEventHandler(BarmanDialog(true)) },
                    isOnlyAbilityUse = false,
                    selectedPlayerName = barmanDataState.barmanChosenName
                )
            } else if (barmanDataState.isBarmanAbilityAvailable) {
                AbilityOrVoteDialog(
                    onConfirm = { _ -> gameRoomViewModel.unitEventHandler(PlayerCardInteractionViaBarman(playerId = barmanDataState.barmanChosenId)) },
                    onCancel = { gameRoomViewModel.unitEventHandler(BarmanDialog(true)) },
                    onDismiss = { gameRoomViewModel.unitEventHandler(BarmanDialog(true)) },
                    isOnlyAbilityUse = true,
                    selectedPlayerName = barmanDataState.barmanChosenName
                )
            } else if (voteDataState.isNotVoted && gameRoomViewModel.areNoAliveMafias(barmanDataState.barmanChosenId)) {
                gameRoomViewModel.unitEventHandler(OnPlayerCardClick(barmanDataState.barmanChosenId, barmanDataState.barmanChosenName))
                gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0))
            }
        }

        if (informatorDataState.informatorDialog) {
            if (informatorDataState.isInformatorAbilityAvailable && voteDataState.isNotVoted && gameRoomViewModel.areNoAliveMafias(informatorDataState.informatorChosenId) && gameRoomViewModel.informatorHelper(informatorDataState.informatorChosenId)) {
                AbilityOrVoteDialog(
                    onConfirm = { isAbilityOrVote ->
                        if (isAbilityOrVote == ability)
                            gameRoomViewModel.unitEventHandler(OnInformatorConfirm(informatorDataState.informatorChosenId))
                        else
                            gameRoomViewModel.unitEventHandler(OnDoubleDialogVoteClick(informatorDataState.informatorChosenId))
                    },
                    onCancel = { gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0)) },
                    onDismiss = { gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0)) },
                    isOnlyAbilityUse = false,
                    selectedPlayerName = informatorDataState.informatorChosenName
                )
            } else if (informatorDataState.isInformatorAbilityAvailable && gameRoomViewModel.informatorHelper(informatorDataState.informatorChosenId)) {
                AbilityOrVoteDialog(
                    onConfirm = { _ -> gameRoomViewModel.unitEventHandler(OnInformatorConfirm(informatorDataState.informatorChosenId)) },
                    onCancel = { gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0)) },
                    onDismiss = { gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0)) },
                    isOnlyAbilityUse = true,
                    selectedPlayerName = informatorDataState.informatorChosenName
                )
            } else if (voteDataState.isNotVoted && gameRoomViewModel.areNoAliveMafias(informatorDataState.informatorChosenId)) {
                gameRoomViewModel.unitEventHandler(OnPlayerCardClick(informatorDataState.informatorChosenId, informatorDataState.informatorChosenName))
                gameRoomViewModel.unitEventHandler(OnInformatorDialog(true, "", 0))
            }
        }

        if (bomberDataState.bomberDialog) {
            if (bomberDataState.isBomberAbilityAvailable) {
                AbilityOrVoteDialog(
                    onConfirm = { action ->
                        gameRoomViewModel.unitEventHandler(
                            OnBomberVoteOrAbilityUse(
                                chosenId = bomberDataState.bomberChosenId,
                                chosenAction = action
                            )
                        )
                    },
                    onCancel = {
                        gameRoomViewModel.unitEventHandler(
                            BomberDialog(
                                isDismiss = true,
                                chosenName = "",
                                chosenId = -1
                            )
                        )
                    },
                    onDismiss = {
                        gameRoomViewModel.unitEventHandler(
                            BomberDialog(
                                isDismiss = true,
                                chosenName = "",
                                chosenId = -1
                            )
                        )
                    },
                    isOnlyAbilityUse = gameRoomViewModel.booleanEventHandler(
                        IsBomberOnlyAbilityUse(currentPlayerRole = currentPlayer?.playerRole!!)
                    ),
                    selectedPlayerName = bomberDataState.bomberChosenName
                )
            }
        }

        if (loverDataState.loverDialog && loverDataState.isLoverAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { gameRoomViewModel.unitEventHandler(OnLoverAbilityUse(chosenId = loverDataState.loverChosenId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(LoverDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(LoverDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = loverDataState.loverChosenName
            )
        }

        if (journalistDataState.journalistDialog && journalistDataState.isJournalistAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { gameRoomViewModel.unitEventHandler(OnJournalistConfirm(journalistDataState.journalistTempId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(JournalistDialog(true, "", -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(JournalistDialog(true, "", -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = journalistDataState.journalistChosenName
            )
        }

        if (doctorDataState.doctorDialog && doctorDataState.isDoctorAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { gameRoomViewModel.unitEventHandler(OnDoctorAbilityUse(chosenId = doctorDataState.doctorChosenId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(DoctorDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(DoctorDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = doctorDataState.doctorChosenName
            )
        }

        if (sherifDataState.sherifDialog && sherifDataState.isSherifAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { gameRoomViewModel.unitEventHandler(OnSherifAbilityUse(chosenId = sherifDataState.sherifChosenId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(SherifDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(SherifDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = sherifDataState.sherifChosenName
            )
        }

        if (detectiveDataState.detectiveDialog && detectiveDataState.isDetectiveAbilityAvailable) {
            AbilityOrVoteDialog(
                onConfirm = { gameRoomViewModel.unitEventHandler(OnDetectiveConfirm(chosenId = detectiveDataState.detectiveChosenId)) },
                onCancel = { gameRoomViewModel.unitEventHandler(OnDetectiveDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                onDismiss = { gameRoomViewModel.unitEventHandler(OnDetectiveDialog(isDismiss = true, chosenName = "", chosenId = -1)) },
                isOnlyAbilityUse = true,
                selectedPlayerName = detectiveDataState.detectiveChosenName
            )
        }
    }
}

@Composable
@Preview
fun MafiaGameRoomPreview() {
    MafiaGameRoom(
        modifier = Modifier,
        gameRoomViewModel = viewModel(),
        onBack = {}
    )
}