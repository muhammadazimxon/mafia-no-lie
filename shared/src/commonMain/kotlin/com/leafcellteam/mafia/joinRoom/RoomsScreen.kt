package com.leafcellteam.mafia.joinRoom

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.MAFIA_ONLINE
import com.leafcellteam.mafia.resources.AVAILABLE_ROOMS
import com.leafcellteam.mafia.resources.roomName
import com.leafcellteam.mafia.resources.players
import com.leafcellteam.mafia.resources.noRoomsAvailable
import com.leafcellteam.mafia.resources.clickUpdateToRefresh
import com.leafcellteam.mafia.resources.no
import com.leafcellteam.mafia.resources.rooms_lowerCase
import com.leafcellteam.mafia.resources.active_lowerCase
import com.leafcellteam.mafia.resources.update
import com.leafcellteam.mafia.resources.random
import com.leafcellteam.mafia.resources.detective
import com.leafcellteam.mafia.resources.baseline_system_update_alt_24
import com.leafcellteam.mafia.resources.baseline_transform_24
import com.leafcellteam.mafia.joinRoom.components.ActionButton
import com.leafcellteam.mafia.joinRoom.components.EnhancedPasswordDialog
import com.leafcellteam.mafia.joinRoom.components.EnhancedRoomItem
import com.leafcellteam.mafia.joinRoom.models.RoomEvent
import com.leafcellteam.mafia.joinRoom.models.RoomsScreenViewModel

@Composable
fun RoomScreen(
    onJoin: () -> Unit,
    onBack: () -> Unit,
    roomsScreenViewModel: RoomsScreenViewModel
) {
    val roomState by roomsScreenViewModel.state.collectAsState()

    val darkBackground = Color(0xFF1E1E2E)
    val cardBackground = Color(0xFF282838)
    val accentColor = Color(0xFF7B68EE)

    LaunchedEffect(Unit) {
        roomsScreenViewModel.onEventHandler(RoomEvent.OnUpdate)
    }

    BackHandler {
        roomsScreenViewModel.resetRooms()
        onBack()
    }

    Scaffold(
        containerColor = darkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 80.dp, y = (-80).dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                                radius = 250f
                            ),
                            shape = CircleShape
                        )
                        .blur(40.dp)
                )
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = (-100).dp, y = 100.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
                                radius = 300f
                            ),
                            shape = CircleShape
                        )
                        .blur(50.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(start = 12.dp, top = 8.dp)
                            .size(48.dp)
                            .align(Alignment.TopStart),
                        shape = CircleShape,
                        tonalElevation = 6.dp,
                        shadowElevation = 4.dp,
                        color = Color.White.copy(alpha = 0.06f),
                        onClick = { onBack() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(Res.string.MAFIA_ONLINE),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(Res.string.AVAILABLE_ROOMS),
                            color = accentColor,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(100.dp),
                            thickness = 2.dp,
                            color = accentColor.copy(alpha = 0.5f)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 15.dp)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(Res.string.roomName), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(stringResource(Res.string.players), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.width(80.dp))
                        }

                        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), color = Color.White.copy(alpha = 0.2f))

                        when {
                            roomState.isLoading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = accentColor)
                                }
                            }
                            roomState.roomsList.isEmpty() -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            painter = painterResource(Res.drawable.detective),
                                            contentDescription = "No rooms",
                                            tint = Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(80.dp)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(text = stringResource(Res.string.noRoomsAvailable), color = Color.White.copy(alpha = 0.6f), fontSize = 18.sp)
                                        Text(text = stringResource(Res.string.clickUpdateToRefresh),color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                                    }
                                }
                            }
                            else -> {
                                val roomsWithIndices = roomState.roomsList
                                    .mapIndexed { index, room -> IndexedValue(index, room) }
                                    .filter { it.value.playerQuantity > 0 }

                                if (roomsWithIndices.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                painter = painterResource(Res.drawable.detective),
                                                contentDescription = stringResource(Res.string.no) + " ${stringResource(Res.string.rooms_lowerCase)}",
                                                tint = Color.White.copy(alpha = 0.6f),
                                                modifier = Modifier.size(80.dp)
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(stringResource(Res.string.no) + stringResource(Res.string.active_lowerCase) + stringResource(Res.string.rooms_lowerCase), color = Color.White.copy(alpha = 0.6f), fontSize = 18.sp)
                                        }
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(roomsWithIndices, key = { it.value.roomId }) { indexedRoom ->
                                            val originalIndex = indexedRoom.index
                                            val room = indexedRoom.value

                                            EnhancedRoomItem(
                                                data = room,
                                                onJoin = {
                                                    if (!room.isClickedAlready) {
                                                        roomsScreenViewModel.onEventHandler(
                                                            RoomEvent.OnJoin(
                                                                it,
                                                                onJoin,
                                                                originalIndex
                                                            )
                                                        )
                                                    }
                                                },
                                                onPasswordDialogOpen = {
                                                    if (!room.isClickedAlready) {
                                                        roomsScreenViewModel.onEventHandler(
                                                            RoomEvent.OnPasswordDialogOpen(
                                                                room.password,
                                                                room.roomId,
                                                                originalIndex
                                                            )
                                                        )
                                                    }
                                                },
                                                host = room.country
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton(stringResource(Res.string.update), Res.drawable.baseline_system_update_alt_24, accentColor) {
                            roomsScreenViewModel.onEventHandler(RoomEvent.OnUpdate)
                        }
                        ActionButton(stringResource(Res.string.random), Res.drawable.baseline_transform_24, accentColor) {
                            roomsScreenViewModel.onEventHandler(RoomEvent.OnRandom(onJoin))
                        }
                    }
                }

                roomState.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            if (roomState.isPasswordDialogOn) {
                EnhancedPasswordDialog(
                    value = roomState.password,
                    onValueChange = { roomsScreenViewModel.onEventHandler(RoomEvent.UpdatePassword(it)) },
                    showPassword = roomState.showPassword,
                    onShowPasswordToggle = { roomsScreenViewModel.onEventHandler(RoomEvent.TogglePasswordVisibility) },
                    onDismiss = { roomsScreenViewModel.onEventHandler(RoomEvent.Dismiss) },
                    onJoinWithPassword = {
                        roomsScreenViewModel.onEventHandler(
                            RoomEvent.JoinWithPassword(
                                roomState.dialogsGuid,
                                onJoin,
                                roomState.joinViaPasswordIndex
                            )
                        )
                    },
                    passwordError = roomState.passwordError,
                    accentColor = accentColor
                )
            }
        }
    }
}

@Preview
@Composable
fun RoomScreenPreview() {
    RoomScreen(
        onJoin = { },
        onBack = {},
        roomsScreenViewModel = viewModel()
    )
}
