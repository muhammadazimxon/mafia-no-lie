package com.example.mafiaonlinejetpackcomposecapi.creatingSection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.InputNumberOfPlayers
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.MinimalDialog
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.PasswordInput
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.RolesChooser
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.RoomName
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.customButton.AnimatedCreateButton
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.CreateRoomRequest
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.MafiaApi
import com.example.mafiaonlinejetpackcomposecapi.roles.Role
import com.example.mafiaonlinejetpackcomposecapi.roles.RoleData
import com.example.mafiaonlinejetpackcomposecapi.viewModel.WaitingRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.components.ConnectingToServerScreen
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateGame(
    modifier: Modifier,
    onCreate: () -> Unit,
    waitingRoomViewModel: WaitingRoomViewModel,
    onBack: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val verticalScrollState = rememberScrollState()
    var rangeFloatValueState by remember { mutableStateOf(5f..15f) } // ✔️✔️
    var textFieldState by remember { mutableStateOf("") }                  // ✔️✔️
    var helpButtonState by remember { mutableStateOf<RoleData?>(null) }
    var passwordState by remember { mutableStateOf("") }                   // ✔️✔️
    val onPasswordValueState = { newValue: String ->
        passwordState = newValue
    }
    var showPasswordState by  remember { mutableStateOf(true) }
    val onChangeShowPasswordState = { newState: Boolean ->
        showPasswordState = newState
    }
    val civilianRoles = remember {
        mutableStateListOf(
            RoleData(Role.Sherif),
            RoleData(Role.Detective),
            RoleData(Role.Doctor),
            RoleData(Role.Lover),
            RoleData(Role.Mimic),
            RoleData(Role.Journalist)
        )
    }
    val mafiaRoles = remember {
        mutableStateListOf(
            RoleData(Role.Barman),
            RoleData(Role.Informator),
            RoleData(Role.Bomber),
            RoleData(Role.Don)
        )
    }
    val onCheckedChangeCivilian = { it: Boolean, clickedPosition: Int ->
        civilianRoles[clickedPosition] = civilianRoles[clickedPosition].copy(isChosen = it)
    }
    val onCheckedChangeMafia = { it: Boolean, clickedPosition: Int ->
        mafiaRoles[clickedPosition] = mafiaRoles[clickedPosition].copy(isChosen = it)
    }
    var isLoading by remember { mutableStateOf(false) }
    val onCreateClick: () -> Unit = {
        coroutine.launch {
            try {
                MafiaApi.retrofitService.createRoom(
                    CreateRoomRequest(
                        roomName = textFieldState,
                        playerName = "Guest",
                        minPlayers = rangeFloatValueState.start.toInt(),
                        maxPlayers = rangeFloatValueState.endInclusive.toInt(),
                        allowedRoles = civilianRoles.filter { it.isChosen }
                            .map { it.role::class.simpleName.toString().uppercase() },
                        password = passwordState
                    )
                )
                val guid = MafiaApi.retrofitService.getGuid()
                waitingRoomViewModel.guid = guid
                waitingRoomViewModel.currentPlayer = "Watch"
                waitingRoomViewModel.startConnection()
                println("Room created")
                isLoading = true
                delay(1000)
                if (waitingRoomViewModel.hubConnection.connectionState == HubConnectionState.CONNECTED) {
                    onCreate()
                    isLoading = false
                }
            }
            catch(e: Exception){
                println("${e.message}Error while creating the room.")
            }
        }
    }
    if(isLoading) {
        ConnectingToServerScreen(text = "Loading...", modifier = modifier)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
            .then(modifier)
            .verticalScroll(verticalScrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        TopAppBar(
//            navigationIcon = {
//
//            },
//            title = {
//                Text(
//                    text = "Creating Game",
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.W500,
//                    color = Color.White
//                )
//            },
//            colors = TopAppBarColors(
//                containerColor = Color(0xFF1E1E2E),
//                navigationIconContentColor = Color.Transparent,
//                titleContentColor = Color.White,
//                actionIconContentColor = Color.Transparent,
//                scrolledContainerColor = Color.Transparent
//            )
//        )
        Text(
            text = "Creating Game",
            fontSize = 30.sp,
            fontWeight = FontWeight.W500,
            color = Color.White
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            RoomName(
                value = textFieldState,
                onValueChange = { textFieldState = it },
                isError = false,
            )
            Spacer(modifier = Modifier.height(5.dp))
            InputNumberOfPlayers(
                value = rangeFloatValueState,
                onValueChange = { newRange ->
                    if(newRange.start.toInt() + 1 != newRange.endInclusive.toInt() && newRange.start.toInt() != newRange.endInclusive.toInt())
                        rangeFloatValueState = newRange
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .background(Color(0xFF2E2E3E))
                    .border(2.dp, Color(0xFF7B68EE), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Civilian roles:",
                        fontSize = 15.sp,

                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                    Text(
                        text = "Default roles: Mafia and Civilian",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
                civilianRoles.forEachIndexed { index, roleData ->
                    RolesChooser(
                        value = roleData.isChosen,
                        position = index,
                        onCheckedChange = { onCheckedChangeCivilian(it, index) },
                        role = roleData.role,
                        imageId = roleData.role.image,
                        helpButtonClick = { clickedPosition ->
                            helpButtonState = civilianRoles[clickedPosition]
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .background(Color(0xFF2E2E3E))
                    .border(2.dp, Color(0xFF7B68EE), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Text(
                    text = "Mafia roles:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.White
                )
                mafiaRoles.forEachIndexed { index, roleData ->
                    RolesChooser(
                        value = roleData.isChosen,
                        position = index,
                        onCheckedChange = { onCheckedChangeMafia(it, index) },
                        role = roleData.role,
                        imageId = roleData.role.image,
                        helpButtonClick = { clickedPosition ->
                            helpButtonState = mafiaRoles[clickedPosition]
                        }
                    )
                }
            }
            if(helpButtonState != null) {
                MinimalDialog(
                    description = helpButtonState?.role?.description ?: "Unknown error."
                ) {
                    helpButtonState = null
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            PasswordInput(
                value = passwordState,
                onValueChange = onPasswordValueState,
                showState = showPasswordState,
                onShowStateChange = onChangeShowPasswordState
            )
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedCreateButton(onCreate = onCreateClick)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CreateGamePreview() {
    CreateGame(modifier = Modifier, {}, viewModel(), {})
}
