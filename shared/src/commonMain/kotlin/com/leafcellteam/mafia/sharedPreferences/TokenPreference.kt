package com.leafcellteam.mafia.sharedPreferences

import com.leafcellteam.mafia.platform.PlatformContext

/**
 * Раньше это был обычный класс поверх android.content.SharedPreferences.
 * Теперь это expect/actual: публичный API (все методы) остаётся ровно
 * таким же, поэтому TokenManager и всё, что его использует, не меняется
 * ни на йоту — меняется только то, ЧЕМ токены реально хранятся на диске:
 *  - Android: SharedPreferences (как и раньше)
 *  - iOS: NSUserDefaults
 *
 * init(context) на Android обязателен (нужен реальный Context).
 * На iOS init(context) ничего не делает — параметр существует только
 * для единообразия вызова из общего кода.
 */
expect class TokenPreferences() {
    fun init(context: PlatformContext)

    fun saveTokens(accessToken: String, refreshToken: String)
    fun saveIfGuest(isGuest: Boolean)
    fun saveAvatarEmoji(emoji: String)
    fun getAvatarEmoji(): String
    fun getIfGuest(): Boolean
    fun clearTokens()
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
}
