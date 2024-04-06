package com.example.myplaylistmaker.settings.data

import android.app.Application
import com.example.myplaylistmaker.App
import com.example.myplaylistmaker.settings.domen.SettingsRepository
import com.example.myplaylistmaker.settings.model.ThemeSettings

class SettingsRepositoryImpl(val app: App):SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
       return ThemeSettings(app.sharedPrefs.getTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        app.switchTheme(settings.theme)
    }
}