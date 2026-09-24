package com.leafcellteam.mafia.appLanguage

import android.content.Context
import android.content.res.Configuration
import com.leafcellteam.mafia.appLanguage.languages.Language
import com.leafcellteam.mafia.logd
import java.util.Locale

/**
 * Переключение локали "на лету" через Configuration — концепция чисто
 * android'ная (Context.resources.updateConfiguration). На iOS смены языка
 * без перезапуска приложения работают совсем иначе (через bundle/UserDefaults),
 * поэтому этот файл целиком остаётся в androidMain, а не в commonMain.
 */
object Local {
    fun setLanguage(context: Context, lang: Language): Context {
        val locale = Locale(
            when (lang) {
                Language.English -> "en"
                Language.Russian -> "ru"
                Language.Uzbek -> "uz"
            }
        )

        logd("LOCAL", locale.language)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }
}
