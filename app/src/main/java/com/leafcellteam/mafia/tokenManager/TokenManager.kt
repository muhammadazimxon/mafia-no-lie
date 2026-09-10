package com.leafcellteam.mafia.tokenManager

import android.util.Log
import com.leafcellteam.mafia.retrofitService.retrofitModel.MafiaApi
import com.leafcellteam.shared.network.models.RefreshTokenRequest
import com.leafcellteam.shared.token.TokenStorageImpl
import com.leafcellteam.shared.token.TokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class TokenManager(
    private val tokenPreferences: TokenStorageImpl,
) : TokenProvider {
    private val refreshMutex = Mutex()

    override fun getAccessToken(): String? {
        return tokenPreferences.getAccessToken()
    }

    override fun getRefreshToken(): String? {
        return tokenPreferences.getRefreshToken()
    }

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
            Log.d("TokenManager", "Attempting to refresh token")

            val response = withContext(Dispatchers.IO) {
                MafiaApi.retrofitService.refreshTokens(RefreshTokenRequest(refreshToken, isGuestStored))
            }

            Log.d("TokenManager", "Token refreshed successfully")
            saveTokens(response.accessToken, response.refreshToken)
            return response.accessToken

        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to refresh token", e)
            clearTokens()
            return null
        }
    }

    override fun clearTokens() {
        Log.d("TokenManager", "Clearing tokens")
        tokenPreferences.clearTokens()
    }
}