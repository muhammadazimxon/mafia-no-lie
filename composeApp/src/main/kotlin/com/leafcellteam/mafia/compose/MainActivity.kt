package com.leafcellteam.mafia.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leafcellteam.mafia.SharedApp
import com.leafcellteam.mafia.appLanguage.initAndroidDataStore
import com.leafcellteam.mafia.initApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Порядок важен: сначала DataStore (нужен applicationContext),
        // потом initApp (поднимает TokenPreferences + Ktor NetworkModule).
        initAndroidDataStore(applicationContext)
        initApp(applicationContext)

        setContent {
            SharedApp()
        }
    }
}
