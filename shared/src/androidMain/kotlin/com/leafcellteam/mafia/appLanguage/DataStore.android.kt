package com.leafcellteam.mafia.appLanguage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * expect fun createLanguageDataStore() не принимает параметров — а Android
 * без Context/applicationContext DataStore не построить. Поэтому один раз,
 * ДО первого обращения к AppSettings, нужно вызвать initAndroidDataStore(...)
 * (это делает MainActivity нового composeApp-модуля при старте).
 */
private lateinit var androidAppContext: Context

fun initAndroidDataStore(applicationContext: Context) {
    androidAppContext = applicationContext
}

internal actual fun createLanguageDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidAppContext.preferencesDataStoreFile("settings")
    }
