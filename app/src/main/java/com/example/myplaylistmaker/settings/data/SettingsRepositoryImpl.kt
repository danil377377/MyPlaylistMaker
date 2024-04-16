package com.example.myplaylistmaker.settings.data

import com.example.myplaylistmaker.utility.App
import com.example.myplaylistmaker.settings.domen.SettingsRepository
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs
import com.example.myplaylistmaker.settings.model.ThemeSettings

class SettingsRepositoryImpl(val sharedPrefs: SettingsSharedPrefs):SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
       return ThemeSettings(sharedPrefs.getTheme())
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        App().switchTheme(settings.theme)
    }
}