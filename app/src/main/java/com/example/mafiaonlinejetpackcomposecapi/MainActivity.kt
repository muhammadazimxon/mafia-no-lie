package com.example.mafiaonlinejetpackcomposecapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
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
import com.example.mafiaonlinejetpackcomposecapi.loginAsGuest.LoginAsGuestScreen
import com.example.mafiaonlinejetpackcomposecapi.mainMenu.MainMenu
import com.example.mafiaonlinejetpackcomposecapi.profileScreen.ProfileScreen
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
import com.example.mafiaonlinejetpackcomposecapi.serializableData.FromScreenData
import com.example.mafiaonlinejetpackcomposecapi.serializableData.GameRoomScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.HistoryScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.JoinGameScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.LogInScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.LoginAsGuestScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.MainMenuScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.ProfileScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.SettingsScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.StoreScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.WaitingRoomScreen
import com.example.mafiaonlinejetpackcomposecapi.serializableData.fromScreen.FromScreen
import com.example.mafiaonlinejetpackcomposecapi.settingsMenuSection.SettingsMenu
import com.example.mafiaonlinejetpackcomposecapi.sharedPreferences.TokenPreferences
import com.example.mafiaonlinejetpackcomposecapi.storeSection.StoreMenu
import com.example.mafiaonlinejetpackcomposecapi.transitionsOfNavigation.NavAnimations
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.WaitingRoom
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.signalRServiceHub.SignalRServiceHub
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomModels.WaitingRoomParam
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

const val PORT_1 = "5020"
const val PORT_2 = "5000"
const val PORT_3 = "5100"
const val DOMAIN = "http://192.168.1.11:5000/"
const val HOST_1 = "26.244.155.168"
const val HOST_3 = "192.168.33.241"
const val HOST_4 = "192.168.46.241"
const val HOST_5 = "192.168.38.157"
const val HOST_6 = "192.168.1.11"
const val HOST_7 = "192.168.29.12"
const val HOST_8 = "192.168.223.241"
const val HOST_9 = "192.168.89.12"
const val HOST_10 = "10.123.19.12"

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

    AnimatedNavHost(
        navController = navController,
        startDestination = LogInScreen.route,
        enterTransition = { NavAnimations.slideInRightAnimation },
        exitTransition = { NavAnimations.slideOutLeftAnimation },
        popEnterTransition = { NavAnimations.slideInLeftAnimation },
        popExitTransition = { NavAnimations.slideOutRightAnimation }
    ) {
        composable(
            LogInScreen.route,
            enterTransition = { NavAnimations.fadeInAnimation },
            exitTransition = { NavAnimations.fadeOutAnimation }
        ) {
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
                }
            )
        }

        composable(
            MainMenuScreen.route,
            enterTransition = { NavAnimations.fadeInAnimation },
            exitTransition = { NavAnimations.scaleOutAnimation },
            popEnterTransition = { NavAnimations.scaleInAnimation }
        ) {
            MainMenu(
                modifier = modifier,
                onCreateGameScreen = {
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
                registerViewModel = registerViewModel
            )
        }

        composable(
            route = ProfileScreen.route,
            enterTransition = { NavAnimations.scaleInAnimation },
            exitTransition = { NavAnimations.slideOutLeftAnimation }
        ) {
            ProfileScreen(registerViewModel = registerViewModel)
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

        composable(
            CreateCharacterScreen.route,
            enterTransition = {
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(animationSpec = tween(400))
            }
        ) {
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

        composable(
            CreateGameScreen.route,
            enterTransition = { NavAnimations.slideInUpAnimation },
            exitTransition = { NavAnimations.slideOutDownAnimation }
        ) {
            CreateGame(
                createGameParams = CreateGameParam(
                    modifier = modifier,
                    onCreate = {
                        WaitingRoomScreen.setFromScreen(FromScreenData(FromScreen.CreateGame))
                        navController.navigate(WaitingRoomScreen.ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() }
                ),
                createGameViewModel = createGameViewModel
            )
        }

        composable(
            JoinGameScreen.route,
            enterTransition = { NavAnimations.slideInUpAnimation },
            exitTransition = { NavAnimations.slideOutDownAnimation }
        ) {
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

        composable(
            WaitingRoomScreen.ROUTE,
            enterTransition = { NavAnimations.slideInRightAnimation },
            exitTransition = { NavAnimations.slideOutLeftAnimation }
        ) {
            WaitingRoom(
                waitingRoomParams = WaitingRoomParam(
                    modifier = modifier,
                    nextRoom = {
                        navController.navigate(GameRoomScreen.ROUTE) {
                            launchSingleTop = true
                        }
                    },
                    onBack = {
                        val destination = when(GameRoomScreen.getFromScreen().value) {
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

        composable(
            GameRoomScreen.ROUTE,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            MafiaGameRoom(
                modifier = modifier,
                gameRoomViewModel = gameRoomViewModel,
                onBack = {
                    val destination = when(GameRoomScreen.getFromScreen().value) {
                        FromScreen.CreateGame -> MainMenuScreen.route
                        FromScreen.JoinRoom -> JoinGameScreen.route
                    }
                    navController.navigate(destination) {
                        popUpTo(destination) { inclusive = true }
                    }
                    gameRoomViewModel.requestLeaveGame()
                    gameRoomViewModel.resetPlayerLogicInfo()
                }
            )
        }

        composable(
            SettingsScreen.route,
            enterTransition = { NavAnimations.scaleInAnimation },
            exitTransition = { NavAnimations.scaleOutAnimation }
        ) {
            SettingsMenu(onBackClick = { navController.popBackStack() })
        }

        composable(
            StoreScreen.route,
            enterTransition = { NavAnimations.scaleInAnimation },
            exitTransition = { NavAnimations.scaleOutAnimation }
        ) {
            StoreMenu(onBackClick = { navController.popBackStack() })
        }

        composable(
            HistoryScreen.route,
            enterTransition = { NavAnimations.slideInRightAnimation },
            exitTransition = { NavAnimations.slideOutLeftAnimation }
        ) {
            GameHistoryScreen(
                modifier = modifier,
                currentPlayerId = registerViewModel.currentPlayerId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            AchievementsScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            AchievementsScreen(
                modifier = modifier,
                viewModel = achievementsViewModel,
                onBack = { navController.popBackStack() }
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