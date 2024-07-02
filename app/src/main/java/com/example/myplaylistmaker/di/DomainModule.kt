package com.example.myplaylistmaker.di

import android.content.Context
import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.impl.MakePlaylistInteractorImpl
import com.example.myplaylistmaker.search.data.network.TracksRepositoryImpl
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.impl.FavoritesInteractorImpl
import com.example.myplaylistmaker.search.domain.impl.TracksInteractorImpl
import com.example.myplaylistmaker.settings.data.SettingsRepositoryImpl
import com.example.myplaylistmaker.settings.data.SettingsSharedPrefsImpl
import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.settings.impl.SettingsInteractorImpl
import com.example.myplaylistmaker.sharing.domen.ExternalNavigator
import com.example.myplaylistmaker.sharing.domen.SharingInteractor
import com.example.myplaylistmaker.sharing.impl.SharingInteractorImpl
import com.example.myplaylistmaker.utility.App
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val interactorModule = module {
    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }
    factory<SettingsInteractor>
    {
        SettingsInteractorImpl(SettingsRepositoryImpl(get()))
    }

    factory<SharingInteractor> {

        SharingInteractorImpl(get(), androidContext())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single<MakePlaylistInteractor> {  MakePlaylistInteractorImpl(get())}


}
