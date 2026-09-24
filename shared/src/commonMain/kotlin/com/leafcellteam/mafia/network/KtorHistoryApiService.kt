package com.leafcellteam.mafia.network

import com.leafcellteam.mafia.historySection.models.PaginatedResult
import com.leafcellteam.mafia.historyService.historyModel.HistoryData
import com.leafcellteam.mafia.historyService.historyModel.PlayerData
import com.leafcellteam.mafia.historyService.historyModel.RatingPlayerData
import com.leafcellteam.mafia.historyService.utils.HistoryApiService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class KtorHistoryApiService(private val client: HttpClient) : HistoryApiService {
    override suspend fun getHistoryDataPaginated(playerId: Int, pageNumber: Int, pageSize: Int): PaginatedResult<HistoryData> =
        client.get("historyData/byPlayer") {
            parameter("playerId", playerId)
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
        }.body()

    override suspend fun getRating(): List<RatingPlayerData> = client.get("rating").body()

    override suspend fun getPlayerData(playerId: Int): PlayerData = 
        client.get("playerData") {
            parameter("playerId", playerId)
        }.body()
}
