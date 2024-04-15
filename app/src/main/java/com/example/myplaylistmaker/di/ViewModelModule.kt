package com.example.myplaylistmaker.di

import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.ui.presentation.TracksSearchViewModel
import com.example.myplaylistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        SettingsViewModel(get(),get())
    }
    viewModel{

        TracksSearchViewModel(get())
    }
    viewModel{

        PlayerViewModel(get())
    }


}