package com.example.myplaylistmaker.di

import android.app.Application
import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.settings.ui.SettingsViewModel
import com.example.myplaylistmaker.sharing.domen.SharingInteractor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ (sharingInteractor: SharingInteractor, settingsInteractor: SettingsInteractor) ->
        SettingsViewModel(sharingInteractor, settingsInteractor)
    }
    viewModel{
        (app: Application) ->
        TracksSearchViewModel(app)
    }
    viewModel{
            (app: Application) ->
        PlayerViewModel(app)
    }


}