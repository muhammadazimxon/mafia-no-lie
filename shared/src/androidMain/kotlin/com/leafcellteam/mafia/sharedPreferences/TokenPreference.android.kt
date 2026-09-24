package com.leafcellteam.mafia.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.leafcellteam.mafia.logd
import com.leafcellteam.mafia.platform.PlatformContext

actual class TokenPreferences actual constructor() {
    private var sharedPreferences: SharedPreferences? = null

    actual fun init(context: PlatformContext) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("my_prefs", android.content.Context.MODE_PRIVATE)
        }
    }

    actual fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences?.edit {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
        }
    }

    actual fun saveIfGuest(isGuest: Boolean) {
        sharedPreferences?.edit { putBoolean("isGuest", isGuest) }
    }

    actual fun saveAvatarEmoji(emoji: String) {
        sharedPreferences?.edit { putString("avatarEmoji", emoji) }
    }

    actual fun getAvatarEmoji(): String = sharedPreferences?.getString("avatarEmoji", "\uD83C\uDFAD") ?: "\uD83C\uDFAD"

    actual fun getIfGuest(): Boolean = sharedPreferences?.getBoolean("isGuest", false) ?: false

    actual fun clearTokens() {
        sharedPreferences?.edit {
            remove("access_token")
            remove("refresh_token")
        }
    }

    actual fun getAccessToken(): String? =
        sharedPreferences?.getString("access_token", null).also {
            logd("TOKEN", "Access token: ${it ?: "нет"}")
        }

    actual fun getRefreshToken(): String? =
        sharedPreferences?.getString("refresh_token", null).also {
            logd("TOKEN", "Refresh token: ${it ?: "нет"}")
        }
}
