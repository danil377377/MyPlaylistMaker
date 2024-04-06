package com.example.myplaylistmaker.settings.domen

import com.example.myplaylistmaker.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}