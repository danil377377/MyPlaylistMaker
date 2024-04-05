package com.example.myplaylistmaker.settings

import com.example.myplaylistmaker.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}