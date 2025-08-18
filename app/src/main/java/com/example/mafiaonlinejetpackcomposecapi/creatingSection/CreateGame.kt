package com.example.mafiaonlinejetpackcomposecapi.creatingSection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.AnimatedCreateButton
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.InputNumberOfPlayers
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.PasswordInput
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.PasswordInputParam
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.RolesChooser
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.RoomName
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.components.RoomNameParam
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameEvent
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameEventWithResult
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameParam
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameViewModel
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.RolesChooserParam
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.dialogs.CustomDialog
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.components.ConnectingToServerScreen

@Composable
fun CreateGame(
    createGameParams: CreateGameParam,
    createGameViewModel: CreateGameViewModel
) {
    val verticalScrollState = rememberScrollState()

    val primaryColor = Color(0xFF7B68EE)
    val backgroundColor = Color(0xFF1E1E2E)
    val cardColor = Color(0xFF2E2E3E)
    val textColor = Color.White

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF32325D),
            backgroundColor
        )
    )

    if(createGameViewModel.createGameState.isLoading) {
        ConnectingToServerScreen(text = "Loading...", modifier = createGameParams.modifier)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .then(createGameParams.modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGradient)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = createGameParams.onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor
                    )
                }

                Text(
                    text = "Create Your Game",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Game Setup",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        RoomName(
                            RoomNameParam(
                                value = createGameViewModel.createGameState.roomName,
                                onValueChange = { newRoomName: String ->
                                    createGameViewModel.createGameEventHandler(CreateGameEvent.UpdateRoomName(newRoomName))
                                },
                                isError = createGameViewModel.createGameState.isErrorRoomName,
                            )
                        )

                        InputNumberOfPlayers(
                            value = createGameViewModel.createGameState.rangeOfPlayers,
                            onValueChange = { newRange ->
                                createGameViewModel.createGameEventHandler(CreateGameEvent.UpdateRangeOfPlayers(newRange))
                            }
                        )
                    }
                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Civilian Roles",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )

                            Text(
                                text = "Default: Mafia & Civilian",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = primaryColor.copy(alpha = 0.3f)
                        )

                        createGameViewModel.createGameState.civilianRoles.forEachIndexed { index, roleData ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (roleData.isChosen) primaryColor.copy(alpha = 0.2f) else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RolesChooser(
                                    RolesChooserParam(
                                        value = roleData.isChosen,
                                        position = index,
                                        onCheckedChange = { isChosen ->
                                            createGameViewModel.createGameEventHandler(
                                                CreateGameEvent.OnChoseCivilianRole(isChosen, index)
                                            )
                                        },
                                        role = roleData.role,
                                        imageId = roleData.role.image,
                                        helpButtonClick = { index ->
                                            createGameViewModel.createGameEventHandler(
                                                CreateGameEvent.CivilianHelpButton(index)
                                            )
                                        },
                                        onClick = { createGameViewModel.handleEventWithResult(event = CreateGameEventWithResult.IsRoleBalanceOK(rolesChooserParam = it)) }
                                    ),
                                )
                            }
                        }
                    }
                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Mafia Roles",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = primaryColor.copy(alpha = 0.3f)
                        )

                        createGameViewModel.createGameState.mafiaRoles.forEachIndexed { index, roleData ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (roleData.isChosen) primaryColor.copy(alpha = 0.2f) else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RolesChooser(
                                    RolesChooserParam(
                                        value = roleData.isChosen,
                                        position = index,
                                        onCheckedChange = { isChosen ->
                                            createGameViewModel.createGameEventHandler(
                                                CreateGameEvent.OnChoseMafiaRole(isChosen, index)
                                            )
                                        },
                                        role = roleData.role,
                                        imageId = roleData.role.image,
                                        helpButtonClick = { index ->
                                            createGameViewModel.createGameEventHandler(
                                                CreateGameEvent.MafiaHelpButton(index)
                                            )
                                        },
                                        onClick = { createGameViewModel.handleEventWithResult(event = CreateGameEventWithResult.IsRoleBalanceOK(rolesChooserParam = it)) }
                                    )
                                )
                            }
                        }
                    }
                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Start Phase",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = primaryColor.copy(alpha = 0.3f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = createGameViewModel.createGameState.isDayPhase,
                                    onClick = {
                                        createGameViewModel.createGameEventHandler(
                                            CreateGameEvent.ChangePhase(
                                                isDay = true,
                                                isNight = false
                                            )
                                        )
                                    },
                                    modifier = Modifier.size(24.dp),
                                    enabled = true,
                                )
                                Text(
                                    modifier = Modifier.clickable {
                                        createGameViewModel.createGameEventHandler(
                                            CreateGameEvent.ChangePhase(
                                                isDay = true,
                                                isNight = false
                                            )
                                        )
                                    },
                                    text = "Day phase",
                                    fontSize = 14.sp,
                                    color = if(createGameViewModel.createGameState.isDayPhase) primaryColor else Color(
                                        116,
                                        116,
                                        116,
                                        255
                                    )
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = createGameViewModel.createGameState.isNightPhase,
                                    onClick = {
                                        createGameViewModel.createGameEventHandler(
                                            CreateGameEvent.ChangePhase(
                                                isDay = false,
                                                isNight = true
                                            )
                                        )
                                    },
                                    modifier = Modifier.size(24.dp),
                                    enabled = true,
                                )
                                Text(
                                    modifier = Modifier.clickable {
                                        createGameViewModel.createGameEventHandler(
                                            CreateGameEvent.ChangePhase(
                                                isDay = false,
                                                isNight = true
                                            )
                                        )
                                    },
                                    text = "Night phase",
                                    fontSize = 14.sp,
                                    color = if(createGameViewModel.createGameState.isNightPhase) primaryColor else Color(
                                        116,
                                        116,
                                        116,
                                        255
                                    )
                                )
                            }
                        }
                    }

                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Room Security",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        PasswordInput(
                            PasswordInputParam(
                                value = createGameViewModel.createGameState.password,
                                onValueChange = { newPassword ->
                                    createGameViewModel.createGameEventHandler(CreateGameEvent.UpdatePassword(newPassword))
                                },
                                showState = createGameViewModel.createGameState.isShowPasswordState,
                                onShowStateChange = { isShown ->
                                    createGameViewModel.createGameEventHandler(CreateGameEvent.TogglePasswordVisibility(isShown))
                                }
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedCreateButton(
                        onCreate = {
                            createGameViewModel.createGameEventHandler(CreateGameEvent.CreateGameAction(createGameParams.onCreate))
                        }
                    )
                }
            }
        }

        if(createGameViewModel.createGameState.helpButtonState != null) {
            CustomDialog(
                title = createGameViewModel.createGameState.helpButtonState!!.role::class.simpleName?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Error",
                text = createGameViewModel.createGameState.helpButtonState!!.role.description,
                roleImage = createGameViewModel.createGameState.helpButtonState!!.role.image,
                confirmText = "OK",
                onConfirm = {
                    createGameViewModel.createGameEventHandler(CreateGameEvent.DismissHelpButton)
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CreateGamePreview() {
    CreateGame(CreateGameParam(Modifier, {}, {}), viewModel())
}