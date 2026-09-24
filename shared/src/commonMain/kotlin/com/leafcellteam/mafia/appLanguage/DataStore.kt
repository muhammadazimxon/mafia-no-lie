package com.leafcellteam.mafia.appLanguage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leafcellteam.mafia.appLanguage.languages.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * androidx.datastore:datastore-preferences реально мультиплатформенный
 * (Android/iOS/desktop), единственное, чем платформы отличаются — это КАК
 * создать сам DataStore<Preferences> (на Android для этого нужен Context,
 * на iOS — просто путь к файлу в песочнице приложения).
 *
 * Поэтому вся логика (сохранить/прочитать язык) — общая, а платформенной
 * остаётся только фабрика самого DataStore.
 */
internal expect fun createLanguageDataStore(): DataStore<Preferences>

object AppSettings {
    private val LANGUAGE_KEY = stringPreferencesKey("language")

    private val store: DataStore<Preferences> by lazy { createLanguageDataStore() }

    suspend fun saveLanguage(lang: Language) {
        val langCode = when (lang) {
            Language.Russian -> "ru"
            Language.Uzbek -> "uz"
            Language.English -> "en"
        }
        store.edit { prefs -> prefs[LANGUAGE_KEY] = langCode }
    }

    suspend fun getLanguage(): Language {
        val code = store.data.map { it[LANGUAGE_KEY] ?: "en" }.first()
        return when (code) {
            "ru" -> Language.Russian
            "uz" -> Language.Uzbek
            else -> Language.English
        }
    }
}
