package com.leafcellteam.mafia.network

import com.leafcellteam.mafia.retrofitService.utils.MafiaApiService
import com.leafcellteam.mafia.historyService.utils.HistoryApiService
import com.leafcellteam.mafia.tokenManager.TokenProvider
import io.ktor.client.*

object NetworkModule {
    private var _client: HttpClient? = null
    val client: HttpClient get() = _client ?: throw IllegalStateException("HttpClient not initialized")

    private var _mafiaService: MafiaApiService? = null
    val mafiaService: MafiaApiService get() = _mafiaService ?: throw IllegalStateException("MafiaApiService not initialized")

    private var _historyService: HistoryApiService? = null
    val historyService: HistoryApiService get() = _historyService ?: throw IllegalStateException("HistoryApiService not initialized")

    fun initialize(tokenProvider: TokenProvider) {
        val ktorClient = createKtorClient(tokenProvider)
        _client = ktorClient
        _mafiaService = KtorMafiaApiService(ktorClient, tokenProvider)
        _historyService = KtorHistoryApiService(ktorClient)
    }
}
