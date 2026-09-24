package com.leafcellteam.mafia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.leafcellteam.mafia.achievementsSection.AchievementsScreen
import com.leafcellteam.mafia.achievementsSection.viewModel.AchievementsViewModel
import com.leafcellteam.mafia.appLanguage.DataStore
import com.leafcellteam.mafia.appLanguage.Local
import com.leafcellteam.mafia.appUpdateManager.rememberAppUpdateManager
import com.leafcellteam.mafia.creatingSection.CreateGame
import com.leafcellteam.mafia.creatingSection.models.CreateGameParam
import com.leafcellteam.mafia.creatingSection.models.CreateGameViewModel
import com.leafcellteam.mafia.gameRoom.MafiaGameRoom
import com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
import com.leafcellteam.mafia.historySection.GameHistoryScreen
import com.leafcellteam.mafia.interseptor.HttpClientBuilder
import com.leafcellteam.mafia.joinRoom.RoomScreen
import com.leafcellteam.mafia.joinRoom.models.RoomsScreenViewModel
import com.leafcellteam.mafia.loginAsGuest.LoginAsGuestScreen
import com.leafcellteam.mafia.mainMenu.MainMenu
import com.leafcellteam.mafia.mainMenu.RoleDescriptions
import com.leafcellteam.mafia.profileScreen.ProfileScreen
import com.leafcellteam.mafia.register.ConfirmEmail
import com.leafcellteam.mafia.register.CreateCharacter
import com.leafcellteam.mafia.register.EnterEmail
import com.leafcellteam.mafia.register.LogIn
import com.leafcellteam.mafia.register.ResetPassword
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.serializableData.AchievementsScreen
import com.leafcellteam.mafia.serializableData.ConfirmRegisterScreen
import com.leafcellteam.mafia.serializableData.CreateCharacterScreen
import com.leafcellteam.mafia.serializableData.CreateGameScreen
import com.leafcellteam.mafia.serializableData.EnterEmailScreen
import com.leafcellteam.mafia.serializableData.FromScreenData
import com.leafcellteam.mafia.serializableData.GameRoomScreen
import com.leafcellteam.mafia.serializableData.HistoryScreen
import com.leafcellteam.mafia.serializableData.JoinGameScreen
import com.leafcellteam.mafia.serializableData.LogInScreen
import com.leafcellteam.mafia.serializableData.LoginAsGuestScreen
import com.leafcellteam.mafia.serializableData.MainMenuScreen
import com.leafcellteam.mafia.serializableData.ProfileScreen
import com.leafcellteam.mafia.serializableData.ResetPasswordScreen
import com.leafcellteam.mafia.serializableData.SettingsScreen
import com.leafcellteam.mafia.serializableData.StoreScreen
import com.leafcellteam.mafia.serializableData.WaitingRoomScreen
import com.leafcellteam.mafia.serializableData.fromScreen.FromScreen
import com.leafcellteam.mafia.settingsMenuSection.SettingsMenu
import com.leafcellteam.mafia.sharedPreferences.TokenPreferences
import com.leafcellteam.mafia.storeSection.StoreMenu
import com.leafcellteam.mafia.tokenManager.TokenManager
import com.leafcellteam.mafia.ui.theme.MafiaOnlineJetpackComposeCAPITheme
import com.leafcellteam.mafia.waitingSection.WaitingRoom
import com.leafcellteam.mafia.waitingSection.signalRServiceHub.SignalRServiceHub
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomParam
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val PORT_1 = "5020"
const val PORT_2 = "5000"
const val PORT_3 = "5100"
//const val DOMAIN = "http://10.168.192.241:5000/"
const val DOMAIN = "https://mafia-publish-production.up.railway.app/"
//const val DOMAIN = "http://10.175.73.241:500/0/"
const val HOST_1 = "26.244.155.168"
const val HOST_3 = "192.168.33.241"
const val HOST_4 = "192.168.46.241"
const val HOST_5 = "192.168.38.157"
const val HOST_6 = "192.168.1.11"
const val HOST_7 = "192.168.29.12"
const val HOST_8 = "192.168.223.241"
const val HOST_9 = "192.168.89.12"
const val HOST_10 = "10.123.19.12"

// BIG TEST EVERYTHING TODO
// CLOSED TEST GOOGLE TODO
// RELEASE TO PRODUCTION TODO

val tokenPreferences = TokenPreferences()
val tokenManager = TokenManager(tokenPreferences)
val okHttpClient = HttpClientBuilder.createOkHttpClient(tokenManager)

class MainActivity : ComponentActivity() {
    private val gameRoomServiceHub = GameRoomServiceHub(lifecycleScope, this)
    private val signalRServiceHub = SignalRServiceHub(gameRoomServiceHub)
    private val gameRoomViewModel = GameRoomViewModel(gameRoomServiceHub, this)
    private val waitingRoomViewModel = WaitingRoomViewModel(signalRServiceHub, gameRoomViewModel)
    private val createGameViewModel = CreateGameViewModel(waitingRoomViewModel, gameRoomViewModel)
    private val roomsScreenViewModel = RoomsScreenViewModel(waitingRoomViewModel)
    private val achievementsViewModel = AchievementsViewModel(this)
    private val registerViewModel = RegisterViewModel(waitingRoomViewModel, achievementsViewModel, tokenManager, this)

    override fun attachBaseContext(context: Context) {
        val language = runBlocking { DataStore.getLanguage(context.applicationContext) }
        val newContext = Local.setLanguage(context, language)
        super.attachBaseContext(newContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getAccessToken()}")
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getRefreshToken()}")
        tokenPreferences.init(this)
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getAccessToken()}")
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getRefreshToken()}")

        enableEdgeToEdge()
        setContent {
            MafiaOnlineJetpackComposeCAPITheme {
                Scaffold { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding),
                        gameRoomViewModel = gameRoomViewModel,
                        waitingRoomViewModel = waitingRoomViewModel,
                        createGameViewModel = createGameViewModel,
                        roomsScreenViewModel = roomsScreenViewModel,
                        registerViewModel = registerViewModel,
                        achievementsViewModel = achievementsViewModel
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        if(gameRoomViewModel.getHubConnection().connectionState == HubConnectionState.CONNECTED) {
            gameRoomViewModel.stopConnection()
        }

        if(waitingRoomViewModel.getHubConnection().connectionState == HubConnectionState.CONNECTED) {
            waitingRoomViewModel.stopConnection()
            waitingRoomViewModel.leaveWaitingRoom()
        }
        super.onDestroy()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(
    modifier: Modifier,
    gameRoomViewModel: GameRoomViewModel,
    waitingRoomViewModel: WaitingRoomViewModel,
    createGameViewModel: CreateGameViewModel,
    roomsScreenViewModel: RoomsScreenViewModel,
    registerViewModel: RegisterViewModel,
    achievementsViewModel: AchievementsViewModel
) {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val localLifeCycle = LocalLifecycleOwner.current

    val updateState = rememberAppUpdateManager()

    LaunchedEffect(Unit) {
        delay(2000)
        updateState.checkForUpdates()
    }

    Box {
        AnimatedNavHost(
            navController = navController,
            startDestination = LogInScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(LogInScreen.route) {
                LogIn(
                    registerViewModel = registerViewModel,
                    onLogIn = {
                        navController.navigate(MainMenuScreen.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRegister = {
                        navController.navigate(EnterEmailScreen.route) {
                            launchSingleTop = true
                        }
                    },
                    onLoginAsGuest = {
                        navController.navigate(LoginAsGuestScreen.route) {
                            launchSingleTop = true
                        }
                    },
                    onResetPassword = {
                        navController.navigate(ResetPasswordScreen.route) {
                            popUpTo(ResetPasswordScreen.route)
                            launchSingleTop = true
                        }
                    },
                    onLanguageSelected = { selectedLang ->
                        localLifeCycle.lifecycleScope.launch {
                            DataStore.saveLanguage(context = context.applicationContext, selectedLang)
                            (context as? ComponentActivity)?.recreate()
    //                        val intent = activity.intent
    //                        activity.finish()
    //                        activity.startActivity(intent)
                        }
                    }
                )
            }

            composable(MainMenuScreen.route) {
                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = {
                        val darkBackground = Color(0xFF1E1E2E)

                        Scaffold(
                            modifier = Modifier.background(Color.Transparent),
                            containerColor = Color.Transparent
                        ) { innerPaddingValues ->
                            RoleDescriptions(innerPaddingValues, sheetState)
                        }
                    },
                    sheetBackgroundColor = Color.Transparent,
                ) {
                    MainMenu(
                        modifier = if (sheetState.isVisible) modifier.blur(20.dp) else modifier,
                        callBack = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        onCreateGameScreen = {
                            createGameViewModel.resetState()
                            navController.navigate(CreateGameScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onJoinGameScreen = {
                            navController.navigate(JoinGameScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onSettingsScreen = {
                            navController.navigate(SettingsScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onStoreScreen = {
                            navController.navigate(StoreScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onHistoryScreen = {
                            navController.navigate(HistoryScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onAchievementsScreen = {
                            navController.navigate(AchievementsScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        registerForGuest = {
                            navController.navigate(EnterEmailScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        onProfileScreen = {
                            navController.navigate(ProfileScreen.route) {
                                launchSingleTop = true
                            }
                        },
                        toLoginScreen = {
                            navController.navigate(LogInScreen.route) {
                                popUpTo(LogInScreen.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        },
                        openSheetState = {
                            coroutineScope.launch {
                                if (sheetState.isVisible) {
                                    sheetState.hide()
                                } else {
                                    sheetState.show()
                                }
                            }
                        },
                        registerViewModel = registerViewModel,
                        context = context,
                        onLanguageSelected = { selectedLang ->
                            localLifeCycle.lifecycleScope.launch {
                                DataStore.saveLanguage(context.applicationContext, selectedLang)

                                val launchIntent = context.packageManager
                                    .getLaunchIntentForPackage(context.packageName)
                                    ?.apply {
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        putExtra("startDestination", LogInScreen.route)
                                    }

                                if (launchIntent != null) {
                                    (context as? ComponentActivity)?.startActivity(launchIntent)
                                    (context as? ComponentActivity)?.overridePendingTransition(0, 0)
                                } else (context as? ComponentActivity)?.recreate()
                            }

                        }
                    )
                }
            }

            composable(ProfileScreen.route) {
                ProfileScreen(
                    registerViewModel = registerViewModel,
                    onBackClick = { navController.popBackStack() })
            }

            composable(EnterEmailScreen.route) {
                EnterEmail(
                    onBack = { navController.popBackStack() },
                    onCheckEmail = {
                        navController.navigate(ConfirmRegisterScreen.route) {
                            launchSingleTop = true
                        }
                    },
                    registerViewModel = registerViewModel
                )
            }

            composable(ConfirmRegisterScreen.route) {
                ConfirmEmail(
                    onBack = { navController.popBackStack() },
                    onConfirm = {
                        navController.navigate(CreateCharacterScreen.route) {
                            launchSingleTop = true
                        }
                    },
                    registerViewModel = registerViewModel
                )
            }

            composable(CreateCharacterScreen.route) {
                CreateCharacter(
                    onBack = { navController.popBackStack() },
                    onConfirm = {
                        navController.navigate(LogInScreen.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    registerViewModel = registerViewModel
                )
            }

            composable(LoginAsGuestScreen.route) {
                LoginAsGuestScreen(
                    onBack = { navController.popBackStack() },
                    registerViewModel = registerViewModel,
                    onContinue = {
                        navController.navigate(MainMenuScreen.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(CreateGameScreen.route) {
                CreateGame(
                    createGameParams = CreateGameParam(
                        modifier = modifier,
                        onCreate = {
                            WaitingRoomScreen.setFromScreen(FromScreenData(FromScreen.CreateGame))
                            navController.navigate(WaitingRoomScreen.ROUTE) {
                                launchSingleTop = true
                            }
                        },
                        onBack = {
                            navController.popBackStack()
                            createGameViewModel.resetState()
                        }
                    ),
                    createGameViewModel = createGameViewModel
                )
            }

            composable(JoinGameScreen.route) {
                RoomScreen(
                    roomsScreenViewModel = roomsScreenViewModel,
                    onJoin = {
                        WaitingRoomScreen.setFromScreen(FromScreenData(FromScreen.JoinRoom))
                        navController.navigate(WaitingRoomScreen.ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(WaitingRoomScreen.ROUTE) {
                WaitingRoom(
                    waitingRoomParams = WaitingRoomParam(
                        modifier = modifier,
                        nextRoom = {
                            navController.navigate(GameRoomScreen.ROUTE) {
                                launchSingleTop = true
                            }
                        },
                        onBack = {
                            val destination = when (GameRoomScreen.getFromScreen().value) {
                                FromScreen.CreateGame -> MainMenuScreen.route
                                FromScreen.JoinRoom -> JoinGameScreen.route
                            }
                            navController.navigate(destination) {
                                popUpTo(destination) { inclusive = true }
                            }
                        }
                    ),
                    waitingViewModel = waitingRoomViewModel
                )
            }

            composable(GameRoomScreen.ROUTE) {
                MafiaGameRoom(
                    modifier = modifier,
                    gameRoomViewModel = gameRoomViewModel,
                    onBack = {
                        val destination = when (GameRoomScreen.getFromScreen().value) {
                            FromScreen.CreateGame -> MainMenuScreen.route
                            FromScreen.JoinRoom -> JoinGameScreen.route
                        }
                        navController.navigate(destination) {
                            popUpTo(destination) { inclusive = true }
                        }
                        if (gameRoomViewModel.getHubConnection().connectionState == HubConnectionState.CONNECTED) {
                            gameRoomViewModel.requestLeaveGame()
                            gameRoomViewModel.resetPlayerLogicInfo()
                        }
                    }
                )
            }

            composable(SettingsScreen.route) {
                SettingsMenu(onBackClick = { navController.popBackStack() })
            }

            composable(StoreScreen.route) {
                StoreMenu(onBackClick = { navController.popBackStack() })
            }

            composable(HistoryScreen.route) {
                GameHistoryScreen(
                    modifier = modifier,
                    currentPlayerId = registerViewModel.currentPlayerId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(AchievementsScreen.route) {
                AchievementsScreen(
                    modifier = modifier,
                    viewModel = achievementsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ResetPasswordScreen.route) {
                ResetPassword(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }

//        NetworkStatusMonitor()
//        AppUpdateDialogs(updateState)
    }
}

@Composable
@Preview(showBackground = true)
fun AppPreview() {
    MafiaOnlineJetpackComposeCAPITheme {
        App(
            modifier = Modifier,
            gameRoomViewModel = viewModel(),
            waitingRoomViewModel = viewModel(),
            createGameViewModel = viewModel(),
            roomsScreenViewModel = viewModel(),
            registerViewModel = viewModel(),
            achievementsViewModel = viewModel()
        )
    }
}