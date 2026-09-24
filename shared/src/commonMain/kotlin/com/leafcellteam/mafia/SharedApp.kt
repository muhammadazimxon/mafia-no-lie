package com.leafcellteam.mafia

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leafcellteam.mafia.achievementsSection.AchievementsScreen
import com.leafcellteam.mafia.creatingSection.CreateGame
import com.leafcellteam.mafia.creatingSection.models.CreateGameParam
import com.leafcellteam.mafia.gameRoom.MafiaGameRoom
import com.leafcellteam.mafia.historySection.GameHistoryScreen
import com.leafcellteam.mafia.joinRoom.RoomScreen
import com.leafcellteam.mafia.loginAsGuest.LoginAsGuestScreen
import com.leafcellteam.mafia.mainMenu.MainMenu
import com.leafcellteam.mafia.profileScreen.ProfileScreen
import com.leafcellteam.mafia.register.ConfirmEmail
import com.leafcellteam.mafia.register.CreateCharacter
import com.leafcellteam.mafia.register.EnterEmail
import com.leafcellteam.mafia.register.LogIn
import com.leafcellteam.mafia.register.ResetPassword
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.storeSection.StoreMenu
import com.leafcellteam.mafia.theme.MafiaOnlineJetpackComposeCAPITheme
import com.leafcellteam.mafia.waitingSection.WaitingRoom
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomParam
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import kotlinx.serialization.Serializable
import com.leafcellteam.mafia.settingsMenuSection.SettingsMenu

/**
 * Type-safe маршруты (Navigation 2.8+ / Compose Multiplatform 1.7+).
 * Каждый объект/класс — отдельный экран; данные, если нужны, передаются
 * прямо через поля класса вместо строковых "путей".
 */
sealed interface Destination {
    @Serializable
    data object Login : Destination

    @Serializable
    data object MainMenu : Destination

    @Serializable
    data object EnterEmail : Destination

    @Serializable
    data object ConfirmEmail : Destination

    @Serializable
    data object CreateCharacter : Destination

    @Serializable
    data object ResetPassword : Destination

    @Serializable
    data object LoginAsGuest : Destination

    @Serializable
    data object CreateGame : Destination

    @Serializable
    data object JoinGame : Destination

    @Serializable
    data object Profile : Destination

    @Serializable
    data object Store : Destination

    @Serializable
    data object History : Destination

    @Serializable
    data object Achievements : Destination

    @Serializable
    data object Settings : Destination
}

const val WAITING_ROOM_ROUTE = "WaitingRoomScreen"
const val GAME_ROOM_ROUTE = "GameRoomScreen"

@Composable
fun SharedApp() {
    MafiaOnlineJetpackComposeCAPITheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val navController: NavHostController = rememberNavController()

            NavHost(navController = navController, startDestination = Destination.Login) {
                composable<Destination.Login> {
                    LogIn(
                        onLogIn = { navController.navigate(Destination.MainMenu) },
                        onRegister = { navController.navigate(Destination.EnterEmail) },
                        onLoginAsGuest = { navController.navigate(Destination.LoginAsGuest) },
                        onResetPassword = { navController.navigate(Destination.ResetPassword) },
                        registerViewModel = registerViewModel,
                        onLanguageSelected = { /* TODO: AppSettings.saveLanguage(lang) + apply locale */ },
                    )
                }

                composable<Destination.MainMenu> {
                    MainMenu(
                        onCreateGameScreen = {
                            navController.navigate(Destination.CreateGame) {
                                popUpTo<Destination.Login> { inclusive = true }
                            }
                        },
                        onJoinGameScreen = { navController.navigate(Destination.JoinGame) },
                        onSettingsScreen = { navController.navigate(Destination.Settings) },
                        onStoreScreen = { navController.navigate(Destination.Store) },
                        onHistoryScreen = { navController.navigate(Destination.History) },
                        onAchievementsScreen = { navController.navigate(Destination.Achievements) },
                        registerForGuest = { navController.navigate(Destination.LoginAsGuest) },
                        onProfileScreen = { navController.navigate(Destination.Profile) },
                        toLoginScreen = {
                            navController.navigate(Destination.Login) {
                                popUpTo<Destination.Login> { inclusive = true }
                            }
                        },
                        openSheetState = { },
                        callBack = { navController.popBackStack() },
                        registerViewModel = registerViewModel,
                        onLanguageSelected = { /* TODO: AppSettings.saveLanguage(lang) + apply locale */ },
                        modifier = Modifier,
                    )
                }

                composable<Destination.EnterEmail> {
                    EnterEmail(
                        onBack = { navController.popBackStack() },
                        onCheckEmail = { navController.navigate(Destination.ConfirmEmail) },
                        registerViewModel = registerViewModel
                    )
                }

                composable<Destination.ConfirmEmail> {
                    ConfirmEmail(
                        onBack = { navController.popBackStack() },
                        onConfirm = { navController.navigate(Destination.CreateCharacter) },
                        registerViewModel = registerViewModel
                    )
                }

                composable<Destination.CreateCharacter> {
                    CreateCharacter(
                        onBack = { navController.popBackStack() },
                        onConfirm = { /* TODO: navigate to MainMenu or waiting */ },
                        registerViewModel = registerViewModel
                    )
                }

                composable<Destination.ResetPassword> {
                    ResetPassword(
                        onBack = { navController.popBackStack() },
                        onSuccess = { navController.navigate(Destination.Login) }
                    )
                }

                composable<Destination.LoginAsGuest> {
                    LoginAsGuestScreen(
                        onBack = { navController.popBackStack() },
                        registerViewModel = registerViewModel,
                        onContinue = { navController.navigate(Destination.MainMenu) }
                    )
                }

                composable<Destination.CreateGame> {
                    CreateGame(
                        createGameParams = CreateGameParam(
                            modifier = Modifier,
                            onCreate = { navController.navigate(WAITING_ROOM_ROUTE) },
                            onBack = { navController.popBackStack() }
                        ),
                        createGameViewModel = createGameViewModel
                    )
                }

                composable<Destination.JoinGame> {
                    RoomScreen(
                        onJoin = { navController.navigate(WAITING_ROOM_ROUTE) },
                        onBack = { navController.popBackStack() },
                        roomsScreenViewModel = roomsScreenViewModel
                    )
                }

                composable(WAITING_ROOM_ROUTE) {
                    WaitingRoom(
                        waitingRoomParams = WaitingRoomParam(
                            modifier = Modifier,
                            nextRoom = { navController.navigate(GAME_ROOM_ROUTE) },
                            onBack = { navController.popBackStack() }
                        ),
                        waitingViewModel = waitingRoomViewModel
                    )
                }

                composable(GAME_ROOM_ROUTE) {
                    MafiaGameRoom(
                        modifier = Modifier,
                        gameRoomViewModel = gameRoomViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Destination.Profile> {
                    ProfileScreen(
                        registerViewModel = registerViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable<Destination.Store> {
                    StoreMenu(onBackClick = { navController.popBackStack() })
                }

                composable<Destination.History> {
                    GameHistoryScreen(
                        currentPlayerId = registerViewModel.currentPlayerId,
                        modifier = Modifier,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Destination.Achievements> {
                    AchievementsScreen(
                        modifier = Modifier,
                        viewModel = achievementsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<Destination.Settings> {
                    SettingsMenu(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}
