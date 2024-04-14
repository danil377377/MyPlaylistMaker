package com.example.myplaylistmaker.di

import com.example.myplaylistmaker.player.data.GlideLoaderImpl
import com.example.myplaylistmaker.player.domain.GlideLoader
import com.example.myplaylistmaker.search.data.network.NetworkClient
import com.example.myplaylistmaker.search.data.network.RetrofitNetworkClient
import com.example.myplaylistmaker.search.data.network.TracksRepositoryImpl
import com.example.myplaylistmaker.search.data.sharedprefs.SharedPrefsImpl
import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.settings.data.SettingsRepositoryImpl
import com.example.myplaylistmaker.settings.data.SettingsSharedPrefsImpl
import com.example.myplaylistmaker.settings.domen.SettingsRepository
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs
import com.example.myplaylistmaker.sharing.domen.ExternalNavigator
import com.example.myplaylistmaker.utility.App
import org.koin.dsl.module

val repositoryModule = module {
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }


    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    factory<SharedPrefs> {
        SharedPrefsImpl(get())
    }
    factory<GlideLoader> {
        GlideLoaderImpl(get())
    }
    factory<SettingsSharedPrefs> { (app: App) ->
        SettingsSharedPrefsImpl(app)
    }
    factory {
        ExternalNavigator()
    }

}