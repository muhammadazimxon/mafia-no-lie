package com.leafcellteam.mafia.sharedPreferences

import com.leafcellteam.mafia.platform.PlatformContext
import platform.Foundation.NSUserDefaults

actual class TokenPreferences actual constructor() {
    private val defaults: NSUserDefaults get() = NSUserDefaults.standardUserDefaults

    // На iOS отдельный "Context" не нужен — NSUserDefaults доступен глобально.
    actual fun init(context: PlatformContext) { /* no-op */ }

    actual fun saveTokens(accessToken: String, refreshToken: String) {
        defaults.setObject(accessToken, forKey = "access_token")
        defaults.setObject(refreshToken, forKey = "refresh_token")
    }

    actual fun saveIfGuest(isGuest: Boolean) {
        defaults.setBool(isGuest, forKey = "isGuest")
    }

    actual fun saveAvatarEmoji(emoji: String) {
        defaults.setObject(emoji, forKey = "avatarEmoji")
    }

    actual fun getAvatarEmoji(): String =
        defaults.stringForKey("avatarEmoji") ?: "\uD83C\uDFAD"

    actual fun getIfGuest(): Boolean = defaults.boolForKey("isGuest")

    actual fun clearTokens() {
        defaults.removeObjectForKey("access_token")
        defaults.removeObjectForKey("refresh_token")
    }

    actual fun getAccessToken(): String? = defaults.stringForKey("access_token")

    actual fun getRefreshToken(): String? = defaults.stringForKey("refresh_token")
}
