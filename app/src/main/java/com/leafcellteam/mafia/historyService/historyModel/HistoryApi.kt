package com.leafcellteam.mafia.historyService.historyModel

import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.historyService.utils.HistoryApiService
import com.leafcellteam.mafia.retrofitService.retrofitModel.retrofit

private const val BASE_URL = DOMAIN

object HistoryApi {
    val retrofitService: HistoryApiService by lazy {
        retrofit.create(HistoryApiService::class.java)
    }
}