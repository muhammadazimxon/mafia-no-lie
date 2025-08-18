package com.example.mafiaonlinejetpackcomposecapi.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

class TokenPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences?.edit {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun clearTokens() {
        sharedPreferences?.edit {
            remove("access_token")
            remove("refresh_token")
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences?.getString("access_token", null).also {
            Log.d("TOKEN", "Access token: ${it ?: "Токена нет :("}")
        }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences?.getString("refresh_token", null).also {
            Log.d("TOKEN", "Refresh token: ${it ?: "Токена нет :("}")
        }
    }
}
