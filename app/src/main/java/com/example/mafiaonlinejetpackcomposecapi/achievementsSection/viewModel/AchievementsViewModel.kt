package com.example.mafiaonlinejetpackcomposecapi.achievementsSection.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mafiaonlinejetpackcomposecapi.achievementsSection.data.AchievementData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryApi
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.PlayerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AchievementsViewModel : ViewModel() {

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

    suspend fun refreshCurrentPlayerHistoryData() {
        _gameHistories.value = HistoryApi.retrofitService.getHistoryData(playerId = playerId)
    }

    fun refreshAchievementsData() {

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
                title = "Being in new atmosphere",
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
                title = "Tend to play in game atmosphere",
                description = "Play in total 600 mins in the game process",
                isAchieved = isDataValid() && getCurrentPlayerDurationByMins() >= 600
            )
        )

    }

    fun changePlayerId(id : Int) {
        playerId = id
    }
}