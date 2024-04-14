package com.example.myplaylistmaker.utility

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.imdbtraining.utility.Creator
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.di.interactorModule
import com.example.myplaylistmaker.di.repositoryModule
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject


class App : Application(), KoinComponent {

    var darkTheme = false
    val sharedPrefs: SettingsSharedPrefs by inject{
        parametersOf(this)
    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(interactorModule,repositoryModule)
        }





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
