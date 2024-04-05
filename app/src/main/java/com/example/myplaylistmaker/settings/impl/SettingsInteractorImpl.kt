package com.example.myplaylistmaker.settings.impl

import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.settings.domen.SettingsRepository
import com.example.myplaylistmaker.settings.model.ThemeSettings

class SettingsInteractorImpl(val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): Boolean{
     return  settingsRepository.getThemeSettings().theme
    }

    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(ThemeSettings((settings)))
    }
}