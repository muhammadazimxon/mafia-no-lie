package com.leafcellteam.mafia.achievementsSection.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leafcellteam.mafia.R
import com.leafcellteam.mafia.achievementsSection.data.AchievementData
import com.leafcellteam.mafia.historyService.historyModel.HistoryApi
import com.leafcellteam.shared.history.HistoryData
import com.leafcellteam.shared.history.PlayerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AchievementsViewModel(val context : Context) : ViewModel() {

    private var _currentPlayerData : MutableStateFlow<PlayerData?> = MutableStateFlow(
        value = null
    )
    private var _gameHistories : MutableStateFlow<List<HistoryData>> = MutableStateFlow(
        emptyList()
    )

    var playerId by mutableIntStateOf(0)

    private val _achievementsData : MutableStateFlow<List<AchievementData>> = MutableStateFlow(
        emptyList()
    )

    val achievementsData : StateFlow<List<AchievementData>> = _achievementsData.asStateFlow()
    val currentPlayerData : StateFlow<PlayerData?> = _currentPlayerData.asStateFlow()
    val gameHistories : StateFlow<List<HistoryData>> = _gameHistories.asStateFlow()

    private fun isDataValid() : Boolean {
        Log.d("IsDataValid", "isDataValid: ${_currentPlayerData.value != null} ")
       return _currentPlayerData.value != null
    }

    private fun getCurrentPlayerDurationByMins() : Int =
        _gameHistories.value.sumOf { it.duration }

    suspend fun refreshPlayerData() {
        _currentPlayerData.value = HistoryApi.retrofitService.getPlayerData(playerId = playerId)
    }

//    suspend fun refreshCurrentPlayerHistoryData() {
//        _gameHistories.value = HistoryApi.retrofitService.getHistoryDataPaginated(playerId = playerId, )
//    }
    suspend fun refreshCurrentPlayerHistoryData() {
        try {
            val allGames = mutableListOf<HistoryData>()
            var currentPage = 1
            var hasNextPage = true

            while (hasNextPage) {
                val paginatedResult = HistoryApi.retrofitService.getHistoryDataPaginated(
                    playerId = playerId,
                    pageNumber = currentPage,
                    pageSize = 100
                )

                allGames.addAll(paginatedResult.items)
                hasNextPage = paginatedResult.hasNextPage
                currentPage++

                Log.d("AchievementsVM", "Loaded page $currentPage, total games: ${allGames.size}")
            }

            _gameHistories.value = allGames
            Log.d("AchievementsVM", "Total games loaded: ${allGames.size}")

        } catch (e: Exception) {
            Log.e("AchievementsVM", "Error loading history: ${e.message}")
            _gameHistories.value = emptyList()
        }
    }

    fun refreshAchievementsData() {
        _achievementsData.value = listOf(
            AchievementData(
                title = context.getString(R.string.firstLook),
                description = context.getString(R.string.playOneGame),
                isAchieved = isDataValid() && _currentPlayerData.value!!.allPlayedGames >= 1
            ),
            AchievementData(
                title = context.getString(R.string.firstWin),
                description = context.getString(R.string.winOneGame),
                isAchieved = isDataValid() && _currentPlayerData.value!!.wonGames >= 1
            ),
            AchievementData(
                title = context.getString(R.string.beingInNewAtmosphere),
                description = context.getString(R.string.playInTotal10MinsInTheGameProcess),
                isAchieved = isDataValid() && getCurrentPlayerDurationByMins() >= 10
            ),
            AchievementData(
                title = context.getString(R.string.topWinner),
                description = context.getString(R.string.win10Games),
                isAchieved = isDataValid() && _currentPlayerData.value!!.wonGames >= 10
            ),
            AchievementData(
                title = context.getString(R.string.activePlayer),
                description = context.getString(R.string.play10Games),
                isAchieved = isDataValid() && _currentPlayerData.value!!.allPlayedGames >= 10
            ),
            AchievementData(
                title = context.getString(R.string.tendToPlayInGameAtmosphere),
                description = context.getString(R.string.playInTotal600MinsInTheGameProcess),
                isAchieved = isDataValid() && getCurrentPlayerDurationByMins() >= 600
            )
        )
    }

    fun changePlayerId(id : Int) {
        playerId = id
    }
}