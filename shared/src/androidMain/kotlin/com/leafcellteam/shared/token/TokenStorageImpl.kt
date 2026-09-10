package com.leafcellteam.shared.token

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenStorageImpl(context: Context) : TokenStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    override fun saveTokens(access: String, refresh: String) {
        sharedPreferences.edit()
            .putString("access_token", access)
            .putString("refresh_token", refresh)
            .apply()
    }

    override fun getAccessToken(): String? =
        sharedPreferences.getString("access_token", null).also {
            Log.d("TOKEN", "Access token: ${it ?: "Токена нет :("}")
        }

    override fun getRefreshToken(): String? =
        sharedPreferences.getString("refresh_token", null).also {
            Log.d("TOKEN", "Refresh token: ${it ?: "Токена нет :("}")
        }

    override fun clearTokens() {
        Log.d("TOKEN", "Clearing tokens")
        sharedPreferences.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }

    override fun saveIfGuest(isGuest: Boolean) {
        sharedPreferences.edit().putBoolean("isGuest", isGuest).apply()
    }

    override fun getIfGuest(): Boolean =
        sharedPreferences.getBoolean("isGuest", false)

    override fun saveAvatarEmoji(emoji: String) {
        sharedPreferences.edit().putString("avatarEmoji", emoji).apply()
    }

    override fun getAvatarEmoji(): String =
        sharedPreferences.getString("avatarEmoji", "🎭") ?: "🎭"
}
