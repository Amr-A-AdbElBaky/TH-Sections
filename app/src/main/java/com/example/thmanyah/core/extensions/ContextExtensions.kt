package com.example.thmanyah.core.extensions

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.changeLanguage(languageIdentifier: String): Context {
    val locale = Locale(languageIdentifier)
    Locale.setDefault(locale)
    val res = this.resources
    val config = Configuration(res.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
