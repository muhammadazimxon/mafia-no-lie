package com.leafcellteam.mafia.historyService.utils

import com.leafcellteam.mafia.historySection.models.PaginatedResult
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.historyService.historyModel.PlayerData
import com.leafcellteam.mafia.historyService.historyModel.RatingPlayerData

interface HistoryApiService {
    suspend fun getHistoryDataPaginated(
        playerId: Int,
        pageNumber: Int = 1,
        pageSize: Int = 20
    ): PaginatedResult<HistoryData>

    suspend fun getRating(): List<RatingPlayerData>

    suspend fun getPlayerData(playerId: Int): PlayerData
}