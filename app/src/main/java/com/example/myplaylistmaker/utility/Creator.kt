package com.example.imdbtraining.utility

import android.app.Application
import android.content.Context
import com.example.myplaylistmaker.App
import com.example.myplaylistmaker.player.data.GlideLoaderImpl
import com.example.myplaylistmaker.search.data.network.RetrofitNetworkClient
import com.example.myplaylistmaker.search.data.network.TracksRepositoryImpl
import com.example.myplaylistmaker.search.data.sharedprefs.SharedPrefsImpl
import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.impl.TracksInteractorImpl
import com.example.myplaylistmaker.settings.data.SettingsRepositoryImpl
import com.example.myplaylistmaker.settings.data.SettingsSharedPrefsImpl
import com.example.myplaylistmaker.settings.domen.SettingsInteractor
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs
import com.example.myplaylistmaker.settings.impl.SettingsInteractorImpl
import com.example.myplaylistmaker.sharing.domen.ExternalNavigator
import com.example.myplaylistmaker.sharing.domen.SharingInteractor
import com.example.myplaylistmaker.sharing.impl.SharingInteractorImpl


object Creator {
    private fun getMoviesRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getMoviesRepository(context))
    }
    fun provideSharedPrefs(context: Context): SharedPrefs{
        return SharedPrefsImpl(context)
    }
    fun provideGlideLoader(context: Context):GlideLoaderImpl{
        return GlideLoaderImpl(context)
    }
    fun provideSettingsSharedPrefs(app: Application): SettingsSharedPrefs {
        return SettingsSharedPrefsImpl(app)
    }
    fun provideSettingsInteractor(app: App): SettingsInteractor {
        return SettingsInteractorImpl(SettingsRepositoryImpl(app))
    }
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(ExternalNavigator(), context)
    }


}