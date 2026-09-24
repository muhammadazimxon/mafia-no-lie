package com.leafcellteam.mafia.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leafcellteam.mafia.appLanguage.AppSettings
import com.leafcellteam.mafia.appLanguage.languages.Language
import com.leafcellteam.mafia.platform.BackHandler
import com.leafcellteam.mafia.creatingSection.components.MinimalDialog
import com.leafcellteam.mafia.register.registerMvi.RegisterEvent
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.tokenManager
import com.leafcellteam.mafia.tokenPreferences
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.MAFIA_ONLINE
import com.leafcellteam.mafia.resources.bePartOfTheStrategy
import com.leafcellteam.mafia.resources.createGame
import com.leafcellteam.mafia.resources.joinGame
import com.leafcellteam.mafia.resources.history
import com.leafcellteam.mafia.resources.profile
import com.leafcellteam.mafia.resources.settings
import com.leafcellteam.mafia.resources.store
import com.leafcellteam.mafia.resources.logOut
import com.leafcellteam.mafia.resources.journalist
import com.leafcellteam.mafia.resources.editor_choice_24px

@Composable
fun MainMenu(
    onCreateGameScreen: () -> Unit,
    onJoinGameScreen: () -> Unit,
    onSettingsScreen: () -> Unit,
    onStoreScreen: () -> Unit,
    onHistoryScreen: () -> Unit,
    onAchievementsScreen: () -> Unit,
    registerForGuest: () -> Unit,
    onProfileScreen: () -> Unit,
    toLoginScreen: () -> Unit,
    openSheetState: () -> Unit,
    callBack: () -> Unit,
    registerViewModel: RegisterViewModel,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier,
) {
    val darkBackground = Color(0xFF1E1E2E)
    val cardBackground = Color(0xFF282838)
    val accentColor = Color(0xFF7B68EE)

    var expanded by remember { mutableStateOf(false) }
    var currentLang by remember { mutableStateOf(Language.English) }
    val languages = listOf(Language.English, Language.Russian, Language.Uzbek)

    LaunchedEffect(true) {
        currentLang = AppSettings.getLanguage()
    }

    BackHandler {
        callBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-50).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                            radius = 300f
                        ),
                        shape = CircleShape
                    )
                    .blur(40.dp)
            )

            Box(
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-100).dp, y = 100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.2f), Color.Transparent),
                            radius = 350f
                        ),
                        shape = CircleShape
                    )
                    .blur(50.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            if(registerViewModel.isGuest || tokenPreferences.getIfGuest()) {
                IconButton(
                    onClick = { registerForGuest() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                                radius = 200f
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "register_for_guest",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            IconButton(
                onClick = { openSheetState() },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                            radius = 200f
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp).padding(start = 1.dp),
                    painter = painterResource(Res.drawable.journalist),
                    contentDescription = "Description",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = {
                    if(registerViewModel.isGuest.not()) onAchievementsScreen()
                    else registerViewModel.callForGuestDialog()
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                            radius = 200f
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.editor_choice_24px),
                    contentDescription = "Achievements",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                                radius = 200f
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Text(
                        text = when(currentLang) {
                            Language.English -> "EN"
                            Language.Russian -> "RU"
                            Language.Uzbek -> "UZ"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(cardBackground)
                ) {
                    languages.forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when(lang) {
                                        Language.English -> "English"
                                        Language.Russian -> "Русский"
                                        Language.Uzbek -> "Oʻzbek"
                                    },
                                    color = Color.White
                                )
                            },
                            onClick = {
                                currentLang = lang
                                onLanguageSelected(lang)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(accentColor.copy(alpha = 0.2f), CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "M",
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = stringResource(Res.string.MAFIA_ONLINE),
                    fontSize = 42.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(Res.string.bePartOfTheStrategy),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Early Access v0.9",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            Card(
                modifier = Modifier
                    .width(320.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                        .clip(RoundedCornerShape((16).dp))
                        .verticalScroll(state = rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EnhancedMenuButton(
                        text = stringResource(Res.string.createGame),
                        onClick = onCreateGameScreen,
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.joinGame),
                        onClick = onJoinGameScreen,
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.history),
                        onClick = if(registerViewModel.isGuest.not()) onHistoryScreen
                        else { { registerViewModel.callForGuestDialog() } },
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.profile),
                        onClick = {
                            if (registerViewModel.isGuest.not()) {
                                onProfileScreen()
                            } else {
                                registerViewModel.callForGuestDialog()
                            }
                        },
                        accentColor = accentColor,
                        isPrimary = true
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(0.8f),
                        color = Color.White.copy(alpha = 0.2f)
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.settings),
                        onClick = onSettingsScreen,
                        accentColor = accentColor,
                        isPrimary = false
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.store),
                        onClick = onStoreScreen,
                        accentColor = accentColor,
                        isPrimary = false
                    )

                    EnhancedMenuButton(
                        text = stringResource(Res.string.logOut),
                        onClick = {
                            tokenManager.saveTokens("", "")
                            tokenManager.saveIfGuest(false)
                            toLoginScreen()
                            if(registerViewModel.isGuest) {
                                registerViewModel.logOutForGuest()
                            }
                        },
                        accentColor = accentColor,
                        isPrimary = false,
                        isLogOut = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(registerViewModel.registerState.collectAsState().value.isTextMessageOn) {
                MinimalDialog(
                    registerViewModel.registerState.collectAsState().value.textMessage
                ) {
                    registerViewModel.registerEventHandler(RegisterEvent.OnDismiss)
                }
            }
        }
    }
}

@Composable
fun EnhancedMenuButton(
    text: String,
    onClick: () -> Unit,
    accentColor: Color,
    isPrimary: Boolean,
    isLogOut: Boolean = false
) {
    val buttonColors = if (isPrimary) {
        ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color.White
        )
    } else if(isLogOut) {
        ButtonDefaults.buttonColors(
            containerColor = Color(0xFF8C2929),
            contentColor = Color.White
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = Color(0xFF32334D),
            contentColor = Color.White
        )
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = buttonColors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isPrimary || isLogOut) 6.dp else 2.dp,
            pressedElevation = if (isPrimary || isLogOut) 10.dp else 4.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun MainMenuPreview() {
    MainMenu(
        onCreateGameScreen = {},
        onJoinGameScreen = {},
        onSettingsScreen = {},
        onStoreScreen = {},
        onHistoryScreen = {},
        onAchievementsScreen = {},
        registerForGuest = {},
        onProfileScreen = {},
        toLoginScreen = {},
        openSheetState = {},
        callBack = {},
        registerViewModel = viewModel(),
        onLanguageSelected = {},
        modifier = Modifier.padding(vertical = 30.dp)
    )
}