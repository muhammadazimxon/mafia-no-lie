package com.leafcellteam.shared.token

import platform.Foundation.NSUserDefaults

class TokenStorageImpl : TokenStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun saveTokens(access: String, refresh: String) {
        defaults.setObject(access, "access_token")
        defaults.setObject(refresh, "refresh_token")
    }

    override fun getAccessToken(): String? = defaults.stringForKey("access_token")
    override fun getRefreshToken(): String? = defaults.stringForKey("refresh_token")

    override fun clearTokens() {
        defaults.removeObjectForKey("access_token")
        defaults.removeObjectForKey("refresh_token")
    }

    override fun saveIfGuest(isGuest: Boolean) {
        defaults.setBool(isGuest, "isGuest")
    }

    override fun getIfGuest(): Boolean = defaults.boolForKey("isGuest")

    override fun saveAvatarEmoji(emoji: String) {
        defaults.setObject(emoji, "avatarEmoji")
    }

    override fun getAvatarEmoji(): String = defaults.stringForKey("avatarEmoji") ?: "🎭"
}
