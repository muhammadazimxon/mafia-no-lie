package com.leafcellteam.mafia.appLanguage

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leafcellteam.shared.localization.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object DataStore {
    private val LANGUAGE_KEY = stringPreferencesKey("language")

    suspend fun saveLanguage(context: Context, lang: Language) {
        val langString = when( lang ) {
            Language.Russian -> "ru"
            Language.Uzbek -> "uz"
            else -> "en"
        }

        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = langString
        }
    }

    suspend fun getLanguage(context: Context): Language {
        val temp = context.dataStore.data
        .map { it[LANGUAGE_KEY] ?: "en" }
            .first()

        return when( temp ) {
            "ru" -> Language.Russian
            "uz" -> Language.Uzbek
            else -> Language.English
        }
    }
}