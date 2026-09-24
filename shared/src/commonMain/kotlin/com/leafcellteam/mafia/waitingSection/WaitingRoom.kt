package com.leafcellteam.mafia.waitingSection
import com.leafcellteam.mafia.logd

import com.leafcellteam.mafia.platform.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.connectingToServer
import com.leafcellteam.mafia.resources.loadingRoomData
import com.leafcellteam.mafia.resources.initializingRoom
import com.leafcellteam.mafia.resources.leaving
import com.leafcellteam.mafia.resources.loading
import com.leafcellteam.mafia.resources.playersConnected
import com.leafcellteam.mafia.resources.min
import com.leafcellteam.mafia.resources.max
import com.leafcellteam.mafia.resources.startingIn
import com.leafcellteam.mafia.resources.sec_lowerCase
import com.leafcellteam.mafia.resources.players
import com.leafcellteam.mafia.resources.roomChat
import com.leafcellteam.mafia.resources.messages_lowerCase
import com.leafcellteam.mafia.resources.timer
import com.leafcellteam.mafia.roles.Role
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.leafcellteam.mafia.waitingSection.components.ConnectingToServerScreen
import com.leafcellteam.mafia.waitingSection.mvi.WaitingRoomEvent
import com.leafcellteam.mafia.waitingSection.waitingRoomBars.WaitingRoomBottomBar
import com.leafcellteam.mafia.waitingSection.waitingRoomBars.WaitingRoomTopBar
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomParam
import com.leafcellteam.mafia.network.HubState
import kotlinx.coroutines.delay

@Composable
fun WaitingRoom(
    waitingRoomParams: WaitingRoomParam,
    waitingViewModel: WaitingRoomViewModel
) {
    val listState = rememberLazyListState()
    val uiState by waitingViewModel.uiState.collectAsState()

    var isInitializing by remember { mutableStateOf(true) }
    var isDataLoaded by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF7B68EE)
    val backgroundColor = Color(0xFF1E1E2E)
    val cardColor = Color(0xFF2E2E3E)
    val accentColor = Color(0xFFFFD700)
    val textColor = Color.White
    val messageCardColor = Color(0xFF363654)

    logd("WaitingRoom", "WaitingRoom composable started")

    DisposableEffect(waitingViewModel.getHubConnection()) {
        logd("WaitingRoom", "DisposableEffect: Setting up event handlers")
        waitingViewModel.setupHubHandler()
        onDispose {
            logd("WaitingRoom", "DisposableEffect: Disposing and stopping connection")
            waitingViewModel.stopConnection()
        }
    }

    LaunchedEffect(true) {
        isInitializing = true
        waitingViewModel.waitingRoomConnection()
    }

    LaunchedEffect(waitingViewModel.getHubConnection().state) {
        waitingViewModel.waitingRoomRequests()
        delay(600)
        isInitializing = false
    }

    if (waitingViewModel.getHubConnection().state == HubState.CONNECTING) {
        logd("WaitingRoom", "Connection state: CONNECTING")
        ConnectingToServerScreen("${stringResource(Res.string.connectingToServer)}...", waitingRoomParams.modifier)
        return
    }

    if (uiState.roomDto == null) {
        logd("WaitingRoom", "roomDto is null, loading data")
        ConnectingToServerScreen("${stringResource(Res.string.loadingRoomData)}...", waitingRoomParams.modifier)
        return
    }

    if(waitingViewModel.gameCounter.collectAsState().value == 0) {
        LaunchedEffect(true) {
            waitingViewModel.waitingRoomCounter { waitingRoomParams.nextRoom() }
        }
    }

    BackHandler(enabled = uiState.backHandlerEnabled) {
        waitingViewModel.backHandlerFunc { waitingRoomParams.onBack() }
    }

    if(isInitializing) {
        ConnectingToServerScreen(stringResource(Res.string.initializingRoom), waitingRoomParams.modifier)
    }

    if (uiState.isBackState) {
        ConnectingToServerScreen(text = "${stringResource(Res.string.leaving)}...", modifier = waitingRoomParams.modifier)
    }

    LaunchedEffect(uiState.messages?.size) {
        if ((uiState.messages?.size ?: 0) > 0) {
            listState.animateScrollToItem(index = (uiState.messages?.size ?: 0) - 1)
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        contentColor = textColor,
        topBar = {
            WaitingRoomTopBar(
                roomName = uiState.roomDto?.roomName ?: "${stringResource(Res.string.loading)}...",
                chosenRoles = uiState.roomDto?.chosenRoles?.mapNotNull { name -> Role.fromName(name)?.image } ?: emptyList(),
                onBack = { waitingViewModel.backHandlerFunc { waitingRoomParams.onBack() } }
            )
        },
        bottomBar = {
            WaitingRoomBottomBar(
                value = waitingViewModel.messageState,
                onValueChange = { newMessage -> waitingViewModel.waitingRoomEventHandler(WaitingRoomEvent.UpdateMessage(newMessage)) },
                onSubmit = { message -> waitingViewModel.waitingRoomEventHandler(WaitingRoomEvent.SubmitMessage(message)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            HorizontalDivider(
                color = primaryColor.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Players",
                            tint = accentColor
                        )
                        Column {
                            Text(
                                text = "${(uiState.roomDto?.players ?: emptyList()).size} ${stringResource(Res.string.playersConnected)}",
                                color = textColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${stringResource(Res.string.min)}: ${uiState.roomDto?.minPlayers ?: 0}, ${stringResource(Res.string.max)}: ${uiState.roomDto?.maxPlayers ?: 0}",
                                color = textColor.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.timer),
                            contentDescription = "Timer",
                            tint = accentColor
                        )
                        Text(
                            text = "${stringResource(Res.string.startingIn)} ${waitingViewModel.gameCounter.collectAsState().value} ${stringResource(Res.string.sec_lowerCase)}",
                            color = textColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.players),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = primaryColor.copy(alpha = 0.3f)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(uiState.roomDto?.players ?: emptyList()) { _, player ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF3A3A5A)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                waitingViewModel.userColors.value.find { it.playerName == player }?.color
                                                    ?: Color.Gray
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (player.isNotEmpty()) player.first().uppercase() else "?",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    Text(
                                        text = player,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.roomChat),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        Text(
                            text = "${uiState.messages?.size ?: 0} ${stringResource(Res.string.messages_lowerCase)}",
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = primaryColor.copy(alpha = 0.3f)
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(uiState.messages ?: emptyList()) { _, message ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = messageCardColor
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(
                                                waitingViewModel.userColors.value.find { it.playerName == message.playerName }?.color
                                                    ?: Color.Gray
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (message.playerName.isNotEmpty()) message.playerName.first().uppercase() else "?",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = message.playerName,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                        }

                                        Text(
                                            text = message.message,
                                            fontSize = 14.sp,
                                            color = textColor.copy(alpha = 0.9f),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun WaitingRoomPreview() {
    WaitingRoom(WaitingRoomParam(Modifier, {}, {}), viewModel())
}