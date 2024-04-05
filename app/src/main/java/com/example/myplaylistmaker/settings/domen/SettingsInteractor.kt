package com.example.myplaylistmaker.settings.domen

import com.example.myplaylistmaker.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)
}