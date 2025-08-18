package com.example.mafiaonlinejetpackcomposecapi.historyService.utils

import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.HistoryData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.PlayerData
import com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel.RatingPlayerData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApiService {

    @GET("/historyData/byPlayer")
    suspend fun getHistoryData(@Query("playerId") playerId: Int) : List<HistoryData>

    @GET("rating")
    suspend fun getRating() : List<RatingPlayerData>

    @GET("/playerData")
    suspend fun getPlayerData(@Query("playerId") playerId : Int) : PlayerData

}