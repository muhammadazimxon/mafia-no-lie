package com.leafcellteam.mafia.appLanguage

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.leafcellteam.shared.localization.Language
import java.util.Locale

object Local {

    fun setLanguage(context : Context, lang : Language) : Context {
        val locale = Locale(when (lang) {
            Language.English -> "en"
            Language.Russian -> "ru"
            Language.Uzbek -> "uz"
        })

        Log.d("LOCAL", locale.language)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }
}