package com.leafcellteam.mafia.tokenManager

interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    suspend fun refreshAccessToken(): String?
    fun clearTokens()
    fun saveTokens(accessToken: String, refreshToken: String)
    fun saveIfGuest(isGuest: Boolean)
}