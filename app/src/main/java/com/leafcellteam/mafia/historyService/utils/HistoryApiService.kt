package com.leafcellteam.mafia.historyService.utils

import com.leafcellteam.mafia.historySection.models.PaginatedResult
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.historyService.historyModel.PlayerData
import com.leafcellteam.mafia.historyService.historyModel.RatingPlayerData
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryApiService {
    @GET("/historyData/byPlayer")
    suspend fun getHistoryDataPaginated(
        @Query("playerId") playerId: Int,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): PaginatedResult<HistoryData>

    @GET("rating")
    suspend fun getRating(): List<RatingPlayerData>

    @GET("/playerData")
    suspend fun getPlayerData(@Query("playerId") playerId: Int): PlayerData
}