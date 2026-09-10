package com.leafcellteam.shared.token

interface TokenStorage {
    fun saveTokens(access: String, refresh: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
    fun saveIfGuest(isGuest: Boolean)
    fun getIfGuest(): Boolean
    fun saveAvatarEmoji(emoji: String)
    fun getAvatarEmoji(): String
}
