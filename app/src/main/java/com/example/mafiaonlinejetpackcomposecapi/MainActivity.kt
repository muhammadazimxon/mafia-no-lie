package com.example.mafiaonlinejetpackcomposecapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.AchievementsScreen
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.viewModel.AchievementsViewModel
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.CreateGame
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameParam
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.models.CreateGameViewModel
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.MafiaGameRoom
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameViewModel.GameRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.historySection.GameHistoryScreen
import com.example.mafiaonlinejetpackcomposecapi.interseptor.HttpClientBuilder
import com.example.mafiaonlinejetpackcomposecapi.interseptor.TokenManager
import com.example.mafiaonlinejetpackcomposecapi.joinRoom.RoomScreen
import com.example.mafiaonlinejetpackcomposecapi.joinRoom.models.RoomsScreenViewModel
import com.example.mafiaonlinejetpackcomposecapi.mainMenu.MainMenu
import com.example.mafiaonlinejetpackcomposecapi.register.ConfirmEmail
import com.example.mafiaonlinejetpackcomposecapi.register.CreateCharacter
import com.example.mafiaonlinejetpackcomposecapi.register.EnterEmail
import com.example.mafiaonlinejetpackcomposecapi.register.LogIn
import com.example.mafiaonlinejetpackcomposecapi.register.registerViewModel.RegisterViewModel
import com.example.mafiaonlinejetpackcomposecapi.serializableData.AchievementsScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.ConfirmRegisterScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.CreateCharacterScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.CreateGameScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.EnterEmailScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.FromScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.GameRoomScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.HistoryScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.JoinGameScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.LogInScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.MainMenuScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.SettingsScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.StoreScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.WaitingRoomScreen
import com.example.mafiaonlinejetpackcomposecapi.settingsMenuSection.SettingsMenu
import com.example.mafiaonlinejetpackcomposecapi.sharedPreferences.TokenPreferences
import com.example.mafiaonlinejetpackcomposecapi.storeSection.StoreMenu
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.WaitingRoom
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.signalRServiceHub.SignalRServiceHub
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.WaitingRoomParam
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel

//const val PORT_1 = "5020"
//const val PORT_2 = "5000"
//const val PORT_3 = "5100"

//const val LOCAL_HOST = "10.0.2.2"
//const val HOST_1 = "192.168.1.10"
//const val HOST_3 = "192.168.33.241"
//const val HOST_4 = "192.168.46.241"
//const val HOST_5 = "192.168.38.157"
//const val HOST_6 = "192.168.1.11"
//const val HOST_7 = "192.168.29.12"
//const val HOST_8 = "192.168.223.241"
//const val HOST_9 = "192.168.89.12"

val tokenPreferences = TokenPreferences()
val tokenManager = TokenManager(tokenPreferences)
val okHttpClient = HttpClientBuilder.createOkHttpClient(tokenManager)

class MainActivity : ComponentActivity() {
    private val gameRoomServiceHub = GameRoomServiceHub()
    private val signalRServiceHub = SignalRServiceHub(gameRoomServiceHub)
    private val gameRoomViewModel = GameRoomViewModel(gameRoomServiceHub)
    private val waitingRoomViewModel = WaitingRoomViewModel(signalRServiceHub, gameRoomViewModel)
    private val createGameViewModel = CreateGameViewModel(waitingRoomViewModel, gameRoomViewModel)
    private val roomsScreenViewModel = RoomsScreenViewModel(waitingRoomViewModel)
    private val achievementsViewModel = AchievementsViewModel()
    private val registerViewModel = RegisterViewModel(waitingRoomViewModel, achievementsViewModel, tokenManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getAccessToken()}")
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getRefreshToken()}")
        tokenPreferences.init(this)
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getAccessToken()}")
        Log.d("TokenPreferences", "onCreate: ${tokenPreferences.getRefreshToken()}")

        enableEdgeToEdge()
        setContent {
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

    override fun onDestroy() {
        super.onDestroy()
        gameRoomViewModel.stopConnection()
        waitingRoomViewModel.stopConnection()
    }
}

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

    NavHost(navController = navController, startDestination = LogInScreen) {
        composable<MainMenuScreen> {
            MainMenu(
                modifier = modifier,
                onCreateGameScreen = {
                    navController.navigate(CreateGameScreen) {
                        popUpTo(CreateGameScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onJoinGameScreen = {
                    navController.navigate(JoinGameScreen) {
                        popUpTo(JoinGameScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSettingsScreen = {
                    navController.navigate(SettingsScreen) {
                        popUpTo(SettingsScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onStoreScreen = {
                    navController. navigate(StoreScreen) {
                        popUpTo(StoreScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onHistoryScreen = {
                    navController.navigate(HistoryScreen) {
                        popUpTo(HistoryScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAchievementsScreen = {
                    navController.navigate(AchievementsScreen) {
                        popUpTo(AchievementsScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<HistoryScreen> {
            GameHistoryScreen(
                modifier = modifier,
                currentPlayerId = registerViewModel.currentPlayerId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<AchievementsScreen> {
            AchievementsScreen(
                modifier = modifier,
                viewModel = achievementsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<CreateGameScreen> {
            CreateGame(
                createGameParams = CreateGameParam(
                    modifier = modifier,
                    onCreate = {
                        navController.navigate(WaitingRoomScreen(FromScreen.CreateGame)) {
                            popUpTo(WaitingRoomScreen(FromScreen.CreateGame)) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                ),
                createGameViewModel = createGameViewModel
            )
        }

        composable<JoinGameScreen> {
            RoomScreen(
                roomsScreenViewModel = roomsScreenViewModel,
                onJoin = {
                    navController.navigate(WaitingRoomScreen(FromScreen.JoinRoom)) {
                        popUpTo(WaitingRoomScreen(FromScreen.JoinRoom)) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<WaitingRoomScreen> { from ->
            WaitingRoom(
                waitingRoomParams = WaitingRoomParam(
                    modifier = modifier,
                    nextRoom = {
                        navController.navigate(GameRoomScreen(from.toRoute<WaitingRoomScreen>().from)) {
                            popUpTo(GameRoomScreen(from.toRoute<WaitingRoomScreen>().from)) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onBack = {
                        val fromScreen = from.toRoute<WaitingRoomScreen>().from
                        navController.navigate(
                            when( fromScreen ) {
                                FromScreen.CreateGame -> MainMenuScreen
                                FromScreen.JoinRoom -> JoinGameScreen
                            }
                        ) {
                            when( fromScreen ) {
                                FromScreen.CreateGame -> navController.popBackStack(MainMenuScreen, true)
                                FromScreen.JoinRoom -> navController.popBackStack(JoinGameScreen, true)
                            }
                        }
                    },
                ),
                waitingViewModel = waitingRoomViewModel,
            )
        }

        composable<GameRoomScreen> { from ->
            MafiaGameRoom(
                modifier = modifier,
                gameRoomViewModel = gameRoomViewModel,
                onBack = {
                    val fromScreen = from.toRoute<GameRoomScreen>().fromRoute
                    navController.navigate(
                        when( fromScreen ) {
                            FromScreen.CreateGame -> MainMenuScreen
                            FromScreen.JoinRoom -> JoinGameScreen
                        }
                    ) {
                        when( fromScreen ) {
                            FromScreen.CreateGame -> navController.popBackStack(MainMenuScreen, true)
                            FromScreen.JoinRoom -> navController.popBackStack(JoinGameScreen, true)
                        }
                    }
                    gameRoomViewModel.requestLeaveGame()
                    gameRoomViewModel.resetPlayerLogicInfo()
                }
            )
        }

        composable<SettingsScreen> {
            SettingsMenu(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<StoreScreen> {
            StoreMenu(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<LogInScreen> {
            LogIn(
                registerViewModel = registerViewModel,
                onLogIn = {
                    navController.navigate(MainMenuScreen) {
                        popUpTo(MainMenuScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onRegister = {
                    navController.navigate(EnterEmailScreen) {
                        popUpTo(EnterEmailScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<EnterEmailScreen> {
            EnterEmail(
                onBack = {
                    navController.popBackStack()
                },
                onCheckEmail = {
                    navController.navigate(ConfirmRegisterScreen) {
                        popUpTo(ConfirmRegisterScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                registerViewModel = registerViewModel
            )
        }

        composable<ConfirmRegisterScreen> {
            ConfirmEmail(
                onBack = {
                    navController.popBackStack()
                },
                onConfirm = {
                    navController.navigate(CreateCharacterScreen) {
                        popUpTo(CreateCharacterScreen) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                registerViewModel = registerViewModel
            )
        }

        composable<CreateCharacterScreen> {
            CreateCharacter(
                onBack = {
                    navController.popBackStack()
                },
                onConfirm = {
                    navController.navigate(LogInScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                registerViewModel = registerViewModel,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppPreview() {
    App(
        modifier = Modifier,
        gameRoomViewModel = GameRoomViewModel(GameRoomServiceHub()),
        waitingRoomViewModel = WaitingRoomViewModel(SignalRServiceHub(GameRoomServiceHub()), GameRoomViewModel(GameRoomServiceHub())),
        createGameViewModel = CreateGameViewModel(WaitingRoomViewModel(SignalRServiceHub(GameRoomServiceHub()), GameRoomViewModel(GameRoomServiceHub())), GameRoomViewModel(GameRoomServiceHub())),
        roomsScreenViewModel = RoomsScreenViewModel(WaitingRoomViewModel(SignalRServiceHub(GameRoomServiceHub()), GameRoomViewModel(GameRoomServiceHub()))),
        registerViewModel = RegisterViewModel(WaitingRoomViewModel(SignalRServiceHub(GameRoomServiceHub()), GameRoomViewModel(GameRoomServiceHub())), AchievementsViewModel(), TokenManager(TokenPreferences())),
        achievementsViewModel = AchievementsViewModel()
    )
}
