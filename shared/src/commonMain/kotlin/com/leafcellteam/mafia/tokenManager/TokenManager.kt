package com.leafcellteam.mafia.tokenManager

import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.mafia.retrofitService.retrofitModel.RefreshTokenRequest
import com.leafcellteam.mafia.sharedPreferences.TokenPreferences
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class TokenManager(
    private val tokenPreferences: TokenPreferences,
) : TokenProvider {
    private val refreshMutex = Mutex()

    override fun getAccessToken(): String? = tokenPreferences.getAccessToken()

    override fun getRefreshToken(): String? = tokenPreferences.getRefreshToken()

    override fun saveTokens(accessToken: String, refreshToken: String) {
        tokenPreferences.saveTokens(accessToken, refreshToken)
    }

    override fun saveIfGuest(isGuest: Boolean) {
        tokenPreferences.saveIfGuest(isGuest)
    }

    override suspend fun refreshAccessToken(): String? = refreshMutex.withLock {
        val refreshToken = getRefreshToken() ?: return null
        val isGuestStored = tokenPreferences.getIfGuest()

        try {
            logd("TokenManager", "Attempting to refresh token")

            val response = withContext(Dispatchers.IO) {
                MafiaApi.retrofitService.refreshTokens(RefreshTokenRequest(refreshToken, isGuestStored))
            }

            logd("TokenManager", "Token refreshed successfully")
            saveTokens(response.accessToken, response.refreshToken)
            return response.accessToken

        } catch (e: Exception) {
            loge("TokenManager", "Failed to refresh token", e)
            clearTokens()
            return null
        }
    }

    override fun clearTokens() {
        logd("TokenManager", "Clearing tokens")
        tokenPreferences.clearTokens()
    }
}
