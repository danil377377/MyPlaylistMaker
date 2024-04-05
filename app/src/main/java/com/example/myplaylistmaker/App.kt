package com.example.myplaylistmaker

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.imdbtraining.utility.Creator
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs

class App : Application() {

    var darkTheme = false
    lateinit var sharedPrefs: SettingsSharedPrefs

    override fun onCreate() {
        super.onCreate()
//        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
         sharedPrefs = Creator.provideSettingsSharedPrefs(this)
        darkTheme = sharedPrefs.getTheme()
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
        sharedPrefs.changeTheme(darkThemeEnabled)


    }

    fun Activity.setAppTheme() {
        val darkTheme = sharedPrefs.getTheme()
        if (darkTheme) {
            setTheme(com.google.android.material.R.style.Theme_Material3_Dark)
        } else {
            setTheme(R.style.Base_Theme_MyPlaylistMaker)
        }
    }
}
