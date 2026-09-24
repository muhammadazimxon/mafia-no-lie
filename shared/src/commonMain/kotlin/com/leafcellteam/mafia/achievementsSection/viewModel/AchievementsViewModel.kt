package com.leafcellteam.mafia.achievementsSection.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leafcellteam.mafia.achievementsSection.data.AchievementData
import com.leafcellteam.mafia.historyService.historyModel.HistoryApi
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.historyService.historyModel.PlayerData
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AchievementsViewModel() : ViewModel() {

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
        logd("IsDataValid", "isDataValid: ${_currentPlayerData.value != null} ")
       return _currentPlayerData.value != null
    }

    private fun getCurrentPlayerDurationByMins() : Int =
        _gameHistories.value.sumOf { it.duration }

    suspend fun refreshPlayerData() {
        _currentPlayerData.value = HistoryApi.retrofitService.getPlayerData(playerId = playerId)
    }

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

                logd("AchievementsVM", "Loaded page $currentPage, total games: ${allGames.size}")
            }

            _gameHistories.value = allGames
            logd("AchievementsVM", "Total games loaded: ${allGames.size}")

        } catch (e: Exception) {
            loge("AchievementsVM", "Error loading history: ${e.message}")
            _gameHistories.value = emptyList()
        }
    }

    fun refreshAchievementsData() {
        // TODO: Use multiplatform resources (Res) to populate these strings in the UI or pass resource keys
        _achievementsData.value = listOf(
            AchievementData(
                title = "First Look",
                description = "Play one game",
                isAchieved = isDataValid() && _currentPlayerData.value!!.allPlayedGames >= 1
            ),
            AchievementData(
                title = "First Win",
                description = "Win one game",
                isAchieved = isDataValid() && _currentPlayerData.value!!.wonGames >= 1
            ),
            AchievementData(
                title = "Being In New Atmosphere",
                description = "Play in total 10 mins in the game process",
                isAchieved = isDataValid() && getCurrentPlayerDurationByMins() >= 10
            ),
            AchievementData(
                title = "Top Winner",
                description = "Win 10 games",
                isAchieved = isDataValid() && _currentPlayerData.value!!.wonGames >= 10
            ),
            AchievementData(
                title = "Active Player",
                description = "Play 10 games",
                isAchieved = isDataValid() && _currentPlayerData.value!!.allPlayedGames >= 10
            ),
            AchievementData(
                title = "Tend To Play In Game Atmosphere",
                description = "Play in total 600 mins in the game process",
                isAchieved = isDataValid() && getCurrentPlayerDurationByMins() >= 600
            )
        )
    }

    fun changePlayerId(id : Int) {
        playerId = id
    }
}
