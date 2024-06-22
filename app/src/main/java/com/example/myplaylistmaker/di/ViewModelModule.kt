package com.example.myplaylistmaker.di

import com.example.myplaylistmaker.media.presentation.FavoritesViewModel
import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import com.example.myplaylistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        SettingsViewModel(get(),get())
    }
    viewModel{

        TracksSearchViewModel(androidApplication(),get())
    }
    viewModel{

        PlayerViewModel(androidApplication(), get(), get(), get())
    }
    viewModel {
        FavoritesViewModel(androidApplication(), get())
    }


}