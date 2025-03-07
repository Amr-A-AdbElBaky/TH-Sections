package com.example.thmanyah.core.presentation.application

import android.app.Application
import android.content.res.Configuration
import com.example.thmanyah.core.extensions.changeLanguage
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ThmanyahApplication : Application() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changeLanguage("ar")
    }

}