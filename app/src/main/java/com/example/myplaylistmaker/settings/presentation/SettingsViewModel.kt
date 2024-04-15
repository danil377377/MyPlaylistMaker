package com.example.myplaylistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.sharing.domen.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    fun getThemeSettings(): Boolean{
       return settingsInteractor.getThemeSettings()
    }
    fun updateThemeSettings(theme: Boolean){
        settingsInteractor.updateThemeSetting(theme)
    }
    fun openTherms(){
        sharingInteractor.openTerms()
    }
    fun shareApp(){
        sharingInteractor.shareApp()
    }
    fun openSupport(){
        sharingInteractor.openSupport()
    }

}