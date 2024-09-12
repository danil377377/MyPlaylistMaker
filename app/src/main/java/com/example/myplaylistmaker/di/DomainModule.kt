package com.example.myplaylistmaker.di

import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.impl.PlaylistInteractorImpl
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.impl.FavoritesInteractorImpl
import com.example.myplaylistmaker.search.domain.impl.TracksInteractorImpl
import com.example.myplaylistmaker.settings.data.SettingsRepositoryImpl
import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.settings.impl.SettingsInteractorImpl
import com.example.myplaylistmaker.sharing.domen.SharingInteractor
import com.example.myplaylistmaker.sharing.impl.SharingInteractorImpl
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
    single<PlaylistInteractor> {  PlaylistInteractorImpl(get(),  get())}


}
