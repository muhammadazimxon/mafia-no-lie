package com.leafcellteam.mafia.historyService.historyModel

import com.leafcellteam.mafia.network.NetworkModule
import com.leafcellteam.mafia.historyService.utils.HistoryApiService

object HistoryApi {
    val retrofitService: HistoryApiService get() = NetworkModule.historyService
}
