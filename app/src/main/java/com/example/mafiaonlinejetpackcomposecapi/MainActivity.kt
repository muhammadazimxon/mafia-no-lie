package com.example.mafiaonlinejetpackcomposecapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mafiaonlinejetpackcomposecapi.creatingSection.CreateGame
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.MafiaGameRoom
import com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection.MainMenu
import com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection.RoomScreen
import com.example.mafiaonlinejetpackcomposecapi.settingsMenuSection.SettingsMenu
import com.example.mafiaonlinejetpackcomposecapi.storeSection.StoreMenu
import com.example.mafiaonlinejetpackcomposecapi.viewModel.WaitingRoomViewModel
import com.example.mafiaonlinejetpackcomposecapi.waitingSection.WaitingRoom
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                App(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Serializable
data object MainMenuScreen

@Serializable
data object CreateGameScreen

@Serializable
data object JoinGameScreen

@Serializable
data object WaitingRoomScreen

@Serializable
data object GameRoomScreen

@Serializable
data object StoreScreen

@Serializable
data object SettingsScreen

@Serializable
data object RegisterScreen

@Composable
fun App(modifier: Modifier) {
    val waitingRoomViewModel: WaitingRoomViewModel = WaitingRoomViewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = RegisterScreen) {
        composable<MainMenuScreen> {
            MainMenu(
                onCreateGameScreen = {
                    navController.navigate(CreateGameScreen) {
                        popUpTo(CreateGameScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onJoinGameScreen = {
                    navController.navigate(JoinGameScreen) {
                        popUpTo(JoinGameScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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
                    navController.navigate(StoreScreen) {
                        popUpTo(StoreScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<CreateGameScreen> {
            CreateGame(
                modifier = modifier,
                onCreate = {
                    navController.navigate(WaitingRoomScreen) {
                        popUpTo(WaitingRoomScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = {

                },
                waitingRoomViewModel = waitingRoomViewModel
            )
        }
        composable<JoinGameScreen> {
            RoomScreen(
                waitingRoomViewModel = waitingRoomViewModel,
                onJoin = {
                    navController.navigate(WaitingRoomScreen) {
                        popUpTo(WaitingRoomScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<WaitingRoomScreen> {
            WaitingRoom(
                modifier = modifier,
                waitingViewModel = waitingRoomViewModel,
                nextRoom = {
                    navController.navigate(GameRoomScreen) {
                        popUpTo(GameRoomScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = {
                    navController.navigate(MainMenuScreen) {
                        popUpTo(MainMenuScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable<GameRoomScreen> {
            MafiaGameRoom(
                modifier = modifier,
                currentPlayerId = 1
            )
        }
        composable<SettingsScreen> {
            SettingsMenu()
        }
        composable<StoreScreen> {
            StoreMenu()
        }
        composable<RegisterScreen> {
            Register(
                waitingViewModel = waitingRoomViewModel,
                onLogIn = {
                    navController.navigate(MainMenuScreen) {
                        popUpTo(GameRoomScreen) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AppPreview() {
    App(modifier = Modifier)
}
