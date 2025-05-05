package com.example.mafiaonlinejetpackcomposecapi.waitingSection

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.viewModel.WaitingRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.components.ConnectingToServerScreen
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomBottomBar.WaitingRoomBottomBar
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomTopBar.WaitingRoomTopBar
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlin.random.Random

//TODO: roomId, roomName, numberOfPlayers, allowedRoles List<???>,
//TODO: FIX: SIZES IN MESSAGES
//TODO: OPTIONAL FIX: SIZE IN PLAYERS NAMES IN FIRST LAZY COLUMN

@Composable
fun WaitingRoom(
    modifier: Modifier,
    nextRoom: () -> Unit,
    waitingViewModel: WaitingRoomViewModel,
    onBack: () -> Unit
) {
    val roomDto by waitingViewModel.waitingRoomDto.collectAsState()
    val isStartState by waitingViewModel.isStartState.collectAsState()
    val messages by waitingViewModel.messages.collectAsState()
    val connectionState = waitingViewModel.hubConnection.connectionState

    Log.d("WaitingRoom", "WaitingRoom composable started")

    if (waitingViewModel.currentPlayer.isBlank()) {
        waitingViewModel.currentPlayer = "Guest"
    }

    DisposableEffect(waitingViewModel.hubConnection) {
        Log.d("WaitingRoom", "DisposableEffect: Setting up event handlers")
        waitingViewModel.setupReceivePlayers()
        waitingViewModel.setupReceiveMessage()
        waitingViewModel.setupSystemMessage()
        waitingViewModel.receiveWaitingRoomData()
        waitingViewModel.setupIsStartGame()
        onDispose {
            Log.d("WaitingRoom", "DisposableEffect: Disposing and stopping connection")
            waitingViewModel.hubConnection.stop()
        }
    }

    LaunchedEffect(true) {
        Log.d("WaitingRoom", "LaunchedEffect(true): Starting connection")
        if (waitingViewModel.guid.isBlank()) {
            Log.e("WaitingRoom", "LaunchedEffect(true): waitingViewModel guid is blank")
        }
        waitingViewModel.startConnection()
    }

    LaunchedEffect(connectionState) {
        Log.d("WaitingRoom", "LaunchedEffect(connectionState): Connection state changed: $connectionState")
        if (connectionState == HubConnectionState.CONNECTED && waitingViewModel.guid.isNotBlank()) {
            Log.d("WaitingRoom", "LaunchedEffect(connectionState): Connected, requesting join game")
            waitingViewModel.requestJoinGame(
                waitingViewModel.guid,
                waitingViewModel.currentPlayer
            )
            waitingViewModel.requestIsStartGame()
        }
    }

    BackHandler(enabled = connectionState == HubConnectionState.CONNECTED) {
        Log.d("WaitingRoom", "BackHandler: Back button pressed")
        try {
            if (connectionState == HubConnectionState.CONNECTED &&
                waitingViewModel.playerId != 0 &&
                waitingViewModel.guid.isNotBlank()
            ) {
                Log.d("WaitingRoom", "BackHandler: Sending leave room request")
                waitingViewModel.sendLeaveRoom(waitingViewModel.guid, waitingViewModel.playerId)
            }
        } catch (e: Exception) {
            Log.e("WaitingRoom", "BackHandler: Error leaving room: ${e.message}")
        } finally {
            Log.d("WaitingRoom", "BackHandler: Stopping connection and navigating back")
            waitingViewModel.hubConnection.stop()
            onBack()
        }
    }

    if (connectionState == HubConnectionState.CONNECTING) {
        Log.d("WaitingRoom", "Connection state: CONNECTING")
        ConnectingToServerScreen("Connecting to server...", modifier)
        return
    }

    if (roomDto == null) {
        Log.d("WaitingRoom", "roomDto is null, loading data")
        ConnectingToServerScreen("Loading room data...", modifier)
        return
    }

    LaunchedEffect(isStartState, waitingViewModel.gameCounter) {
        Log.d(
            "WaitingRoom", "LaunchedEffect(isStartState, gameCounter): isStartState = $isStartState, gameCounter = ${waitingViewModel.gameCounter}"
        )
        if (isStartState) {
            while (waitingViewModel.gameCounter > 0) {
                delay(1000)
                waitingViewModel.gameCounter -= 1
                Log.d(
                    "WaitingRoom", "LaunchedEffect(isStartState, gameCounter): gameCounter decremented to ${waitingViewModel.gameCounter}"
                )
            }
            Log.d("WaitingRoom", "LaunchedEffect(isStartState, gameCounter): Navigating to next room")
            nextRoom()
        }
    }

    val playersList = roomDto?.players ?: emptyList()
    val minPlayers = roomDto?.minPlayers ?: 0
    val maxPlayers = roomDto?.maxPlayers ?: 0
    val roomName = roomDto?.roomName ?: "Loading..."

    val roles = remember(roomDto) {
        roomDto?.chosenRoles?.mapNotNull { name ->
            Role.fromName(name)?.image
        } ?: emptyList()
    }

    val onMessageValueChange = { newMessage: String ->
        waitingViewModel.messageState = newMessage
    }

    val onSubmitMessage = { newMessage: String ->
        Log.d("WaitingRoom", "onSubmitMessage: Sending message: $newMessage")
        if (newMessage.isNotBlank()) {
            waitingViewModel.sendPlayerMessage(
                waitingViewModel.guid,
                waitingViewModel.currentPlayer,
                newMessage
            )
            waitingViewModel.messageState = ""
        }
    }

    val random = remember { Random(System.currentTimeMillis()) }
    val playerColors = remember(playersList) {
        playersList.map {
            Color(
                red = random.nextInt(100, 256),
                green = random.nextInt(100, 256),
                blue = random.nextInt(100, 256)
            )
        }
    }

    val messageColors = remember(messages) {
        messages.map { message ->
            val playerIndex = playersList.indexOfFirst { it == message.user  }
            if (playerIndex >= 0 && playerIndex < playerColors.size) {
                playerColors[playerIndex]
            } else {
                Color(
                    red = random.nextInt(100, 256),
                    green = random.nextInt(100, 256),
                    blue = random.nextInt(100, 256)
                )
            }
        }
    }

    val lazyPaddingValue = 25.dp

    Scaffold(
        containerColor = Color(0xFF1E1E2E),
        contentColor = Color.White,
        topBar = {
            WaitingRoomTopBar(
                roomName = roomName,
                chosenRoles = roles,
            )
        },
        bottomBar = {
            WaitingRoomBottomBar(
                value = waitingViewModel.messageState,
                onValueChange = onMessageValueChange,
                onSubmit = onSubmitMessage
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 6.dp)
        ) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Players in the room [${minPlayers}/${maxPlayers}]: ${playersList.size}",
                    color = Color(0xFFFFD700)
                )
                Text(
                    text = "Game will start in ${waitingViewModel.gameCounter} sec",
                    color = Color(0xFFFFD700)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(Color(0xFF2E2E3E))
                    .border(2.dp, Color(0xFF7B68EE), RoundedCornerShape(12.dp))
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(lazyPaddingValue),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(playersList) { index, player ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(if (index < playerColors.size) playerColors[index] else Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (player.isNotEmpty()) player.first().uppercase() else "?",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                            Text(
                                text = player,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
                    .fillMaxSize()
                    .background(Color(0xFF2E2E3E))
                    .border(2.dp, Color(0xFF7B68EE), RoundedCornerShape(12.dp))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = lazyPaddingValue, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(messages) { index, message ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size((18 + 8 + 16).dp)
                                    .clip(CircleShape)
                                    .background(if (index < messageColors.size) messageColors[index] else Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (message.user.isNotEmpty()) message.user.first().uppercase() else "?",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.width(120.dp),
                                    text = message.user,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    maxLines = 1,
                                    minLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    modifier = Modifier
                                        .widthIn(max = 250.dp)
                                        .wrapContentHeight(),
                                    text = message.message,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W400,
                                    softWrap = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WaitingRoomPreview() {
    WaitingRoom(modifier = Modifier, {}, viewModel(), {})
}