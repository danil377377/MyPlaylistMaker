package com.example.myplaylistmaker.settings.domen

interface SettingsSharedPrefs {
    fun getTheme(): Boolean
    fun changeTheme(theme: Boolean)
}