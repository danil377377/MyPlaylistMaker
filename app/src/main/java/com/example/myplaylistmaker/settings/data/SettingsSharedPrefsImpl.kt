package com.example.myplaylistmaker.settings.data

import android.app.Application

import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs

class SettingsSharedPrefsImpl(app: Application): SettingsSharedPrefs {
    companion object{
        const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
        const val EDIT_SWITCH_KEY = "key_for_edit_text"
    }
    val sharedPrefs = app.getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(EDIT_SWITCH_KEY, false)
    }

    override fun changeTheme(theme: Boolean) {
        sharedPrefs.edit().putBoolean(EDIT_SWITCH_KEY, theme)
            .apply()
    }
}