package com.leafcellteam.mafia.creatingSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.creatingSection.components.AnimatedCreateButton
import com.leafcellteam.mafia.creatingSection.components.InputNumberOfPlayers
import com.leafcellteam.mafia.creatingSection.components.PasswordInput
import com.leafcellteam.mafia.creatingSection.components.PasswordInputParam
import com.leafcellteam.mafia.creatingSection.components.RolesChooser
import com.leafcellteam.mafia.creatingSection.components.RoomName
import com.leafcellteam.mafia.creatingSection.components.RoomNameParam
import com.leafcellteam.mafia.creatingSection.countrySelection.LanguageSelectorCard
import com.leafcellteam.mafia.creatingSection.models.CreateGameEvent
import com.leafcellteam.mafia.creatingSection.models.CreateGameEventWithResult
import com.leafcellteam.mafia.creatingSection.models.CreateGameParam
import com.leafcellteam.mafia.creatingSection.models.CreateGameViewModel
import com.leafcellteam.mafia.creatingSection.models.RolesChooserParam
import com.leafcellteam.mafia.gameRoom.components.dialogs.CustomDialog
import com.leafcellteam.mafia.waitingSection.components.ConnectingToServerScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateGame(
    createGameParams: CreateGameParam,
    createGameViewModel: CreateGameViewModel
) {
    val verticalScrollState = rememberScrollState()
    val isClicked = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val selectedLanguage = remember { mutableStateOf("EN") }

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
        ConnectingToServerScreen(text = stringResource(R.string.loading) + "...", modifier = createGameParams.modifier)
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
                    text = stringResource(R.string.createYourGame),
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
                            text = stringResource(R.string.gameSetup),
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
                                text = stringResource(R.string.civilianRoles),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )

                            Text(
                                text = stringResource(R.string.defaultMafiaAndCivilian),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.LightGray
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
                            text = stringResource(R.string.mafiaRoles),
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

                LanguageSelectorCard(
                    selectedLanguage = selectedLanguage.value,
                    onLanguageSelected = { languageCode ->
                        selectedLanguage.value = languageCode
                    }
                )

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
                            text = stringResource(R.string.roomSecurity),
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
                            if(createGameViewModel.createGameState.isErrorRoomName.not()) {
                                if (isClicked.value) return@AnimatedCreateButton
                                isClicked.value = true

                                CoroutineScope(Dispatchers.IO).launch {
                                    createGameViewModel.createGameEventHandler(
                                        CreateGameEvent.CreateGameAction(
                                            createGameParams.onCreate,
                                            selectedLanguage.value
                                        )
                                    )
                                    delay(1700)
                                    isClicked.value = false
                                }
                            } else {
                                coroutineScope.launch {
                                    verticalScrollState.animateScrollTo(0)
                                }
                            }
                        }
                    )
                }
            }
        }

        if(createGameViewModel.createGameState.helpButtonState != null) {
            CustomDialog(
                title = createGameViewModel.createGameState.helpButtonState!!.role::class.simpleName?.lowercase()?.replaceFirstChar { it.uppercase() } ?: stringResource(R.string.error),
                text = stringResource(createGameViewModel.createGameState.helpButtonState!!.role.descriptionRes),
                roleImage = createGameViewModel.createGameState.helpButtonState!!.role.image,
                confirmText = stringResource(R.string.ok),
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