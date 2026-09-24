//package com.leafcellteam.mafia
//
//import com.leafcellteam.mafia.achievementsSection.viewModel.AchievementsViewModel
//import com.leafcellteam.mafia.appLanguage.AppSettings
//import com.leafcellteam.mafia.creatingSection.models.CreateGameViewModel
//import com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
//import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
//import com.leafcellteam.mafia.joinRoom.models.RoomsScreenViewModel
//import com.leafcellteam.mafia.network.NetworkModule
//import com.leafcellteam.mafia.platform.PlatformContext
//import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
//import com.leafcellteam.mafia.sharedPreferences.TokenPreferences
//import com.leafcellteam.mafia.tokenManager.TokenManager
//import com.leafcellteam.mafia.waitingSection.signalRServiceHub.SignalRServiceHub
//import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob
//
///**
// * Раньше это были обычные top-level val в MainActivity.kt (:app), в том же
// * пакете, что и все экраны — поэтому `import com.leafcellteam.mafia.tokenManager`
// * из экранов работал "бесплатно". При переносе экранов в :shared эти
// * объявления никто не перенёс, из-за чего почти все файлы, которые их
// * импортируют, не резолвились.
// *
// * initApp(context) нужно вызвать один раз при старте приложения, до того,
// * как отрисуется первый экран:
// *  - Android: в MainActivity.onCreate(), передав applicationContext
// *  - iOS: в момент создания MainViewController(), передав PlatformContext()
// */
//val tokenPreferences = TokenPreferences()
//val tokenManager = TokenManager(tokenPreferences)
//
///** Общий скоуп для соединений, которые должны жить, пока живо приложение (не экран). */
//val appScope = CoroutineScope(SupervisorJob())
//
//val gameRoomServiceHub = GameRoomServiceHub(
//    scope = appScope,
//    getLanguage = { AppSettings.getLanguage() }
//)
//val gameRoomViewModel = GameRoomViewModel(gameRoomServiceHub)
//val signalRServiceHub = SignalRServiceHub(gameRoomServiceHub)
//val waitingRoomViewModel = WaitingRoomViewModel(signalRServiceHub, gameRoomViewModel)
//val achievementsViewModel = AchievementsViewModel()
//val registerViewModel = RegisterViewModel(waitingRoomViewModel, achievementsViewModel, tokenManager)
//val createGameViewModel = CreateGameViewModel(waitingRoomViewModel, gameRoomViewModel)
//val roomsScreenViewModel = RoomsScreenViewModel(waitingRoomViewModel)
//
//private var initialized = false
//
//fun initApp(context: PlatformContext) {
//    if (initialized) return
//    initialized = true
//
//    tokenPreferences.init(context)
//    NetworkModule.initialize(tokenManager)
//}
//


package com.leafcellteam.mafia

import com.leafcellteam.mafia.achievementsSection.viewModel.AchievementsViewModel
import com.leafcellteam.mafia.appLanguage.AppSettings
import com.leafcellteam.mafia.creatingSection.models.CreateGameViewModel
import com.leafcellteam.mafia.gameRoom.gameRoomSignalRClient.GameRoomServiceHub
import com.leafcellteam.mafia.gameRoom.gameViewModel.GameRoomViewModel
import com.leafcellteam.mafia.joinRoom.models.RoomsScreenViewModel
import com.leafcellteam.mafia.network.NetworkModule
import com.leafcellteam.mafia.platform.PlatformContext
import com.leafcellteam.mafia.register.registerViewModel.RegisterViewModel
import com.leafcellteam.mafia.sharedPreferences.TokenPreferences
import com.leafcellteam.mafia.tokenManager.TokenManager
import com.leafcellteam.mafia.network.SignalRServiceHub
import com.leafcellteam.mafia.waitingSection.waitingRoomViewModel.WaitingRoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Раньше это были обычные top-level val в MainActivity.kt (:app), в том же
 * пакете, что и все экраны — поэтому `import com.leafcellteam.mafia.tokenManager`
 * из экранов работал "бесплатно". При переносе экранов в :shared эти
 * объявления никто не перенёс, из-за чего почти все файлы, которые их
 * импортируют, не резолвились.
 *
 * initApp(context) нужно вызвать один раз при старте приложения, до того,
 * как отрисуется первый экран:
 *  - Android: в MainActivity.onCreate(), передав applicationContext
 *  - iOS: в момент создания MainViewController(), передав PlatformContext()
 */
val tokenPreferences = TokenPreferences()
val tokenManager = TokenManager(tokenPreferences)

/** Общий скоуп для соединений, которые должны жить, пока живо приложение (не экран). */
val appScope = CoroutineScope(SupervisorJob())

val gameRoomServiceHub = GameRoomServiceHub(
    scope = appScope,
    getLanguage = { AppSettings.getLanguage() }
)
val gameRoomViewModel = GameRoomViewModel(gameRoomServiceHub)
val signalRServiceHub = SignalRServiceHub(gameRoomServiceHub)
val waitingRoomViewModel = WaitingRoomViewModel(signalRServiceHub, gameRoomViewModel)
val achievementsViewModel = AchievementsViewModel()
val registerViewModel = RegisterViewModel(waitingRoomViewModel, achievementsViewModel, tokenManager)
val createGameViewModel = CreateGameViewModel(waitingRoomViewModel, gameRoomViewModel)
val roomsScreenViewModel = RoomsScreenViewModel(waitingRoomViewModel)

private var initialized = false

fun initApp(context: PlatformContext) {
    if (initialized) return
    initialized = true

    tokenPreferences.init(context)
    NetworkModule.initialize(tokenManager)
}
