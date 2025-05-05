package com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.viewModel.WaitingRoomViewModel
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RoomData(
    val roomId: String,
    val roomName: String,
    val password: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playerQuantity: Int
)

@Composable
fun RoomScreen(
    onJoin: () -> Unit,
    waitingRoomViewModel: WaitingRoomViewModel
) {
    var isPasswordDialogOn by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }
    var showState by remember { mutableStateOf(true) }
    var roomsList by remember { mutableStateOf(listOf<RoomData>()) }
    val coroutineScope = rememberCoroutineScope()
    val onJoinClick = { guid: String ->
        try {
            waitingRoomViewModel.guid = guid
            if (guid.isBlank()) {
                Log.e("JOIN", "GUID is blank!")
            }
            waitingRoomViewModel.startConnection()
            coroutineScope.launch {
                delay(500)
                if (waitingRoomViewModel.hubConnection.connectionState == HubConnectionState.CONNECTED) {
                    onJoin()
                } else {
                    Log.d("ROOM SCREEN", "RoomScreen: Error")
                }
            }
        } catch (e: Exception) {
            Log.e("JOIN", "Error joining room: ${e.message}")
        }
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        try {
            roomsList = MafiaApi.retrofitService.update()
        } catch (e: Exception) {
            Log.d("UPDATE", "${e.message}")
        }
    }
    val onUpdate: () -> Unit = {
        scope.launch {
            try {
                roomsList = MafiaApi.retrofitService.update()
            } catch (e: Exception) {
                Log.d("UPDATE", "${e.message}")
            }
        }
    }
    Scaffold(
        containerColor = Color(0xFF1E1E2E)
    ) { padding ->
        if( isPasswordDialogOn )
            PasswordEnter(
                onValueChange = { value = it },
                value = value,
                showState = showState,
                onShowStateChange = { showState = !showState },
                onDismiss = {
                    isPasswordDialogOn = false
                }
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF1E1E2E))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rooms",
                modifier = Modifier
                    .padding(vertical = 50.dp),
                color = Color.White,
                fontSize = 50.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .height(500.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(roomsList) { _, element ->
                        Room(
                            id = element.roomId,
                            quantity = element.playerQuantity,
                            name = element.roomName,
                            playersRange = element.minPlayers..element.maxPlayers,
                            password = element.password,
                            onJoin = { onJoinClick(it) },
                            onPasswordDialogOpen = { isPasswordDialogOn = true }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .width(300.dp)
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Button(
                    onClick = onUpdate,
                    modifier = Modifier
                        .width(130.dp)
                        .height(55.dp)
                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B68EE),
                        contentColor = Color.White
                    )
                ) { Text(
                    text = "Update",
                    fontSize = 22.sp
                ) }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .width(130.dp)
                        .height(55.dp)
                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B68EE),
                        contentColor = Color.White
                    )
                ) { Text(
                    text = "Random",
                    fontSize = 22.sp
                ) }
            }
        }
    }
}

@Preview
@Composable
fun RoomScreenPreview() {
    RoomScreen({}, viewModel())
}