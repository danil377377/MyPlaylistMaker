package com.example.myplaylistmaker.settings.data

import android.app.Application
import com.example.myplaylistmaker.settings.EDIT_SWITCH_KEY
import com.example.myplaylistmaker.settings.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs

class SettingsSharedPrefsImpl(app: Application): SettingsSharedPrefs {
    val sharedPrefs = app.getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, Application.MODE_PRIVATE)
    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(EDIT_SWITCH_KEY, false)
    }

    override fun changeTheme(theme: Boolean) {
        sharedPrefs.edit().putBoolean(EDIT_SWITCH_KEY, theme)
            .apply()
    }
}