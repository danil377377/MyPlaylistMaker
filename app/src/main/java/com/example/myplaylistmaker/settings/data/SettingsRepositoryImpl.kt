package com.example.myplaylistmaker.settings.data

import com.example.myplaylistmaker.utility.App
import com.example.myplaylistmaker.settings.domen.SettingsRepository
import com.example.myplaylistmaker.settings.model.ThemeSettings

class SettingsRepositoryImpl():SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
       return ThemeSettings(App().sharedPrefs.getTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        App().switchTheme(settings.theme)
    }
}