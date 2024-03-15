package com.example.myplaylistmaker.data.sharedprefs

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.myplaylistmaker.presentation.ui.EDIT_SWITCH_KEY
import com.example.myplaylistmaker.presentation.ui.PRACTICUM_EXAMPLE_PREFERENCES

class App : Application() {

    var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(EDIT_SWITCH_KEY, false)
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit().putBoolean(EDIT_SWITCH_KEY, darkThemeEnabled)
            .apply()


    }
}