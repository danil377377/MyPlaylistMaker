package com.example.imdbtraining.utility

import android.app.Application
import android.content.Context
import com.example.myplaylistmaker.player.data.GlideLoaderImpl
import com.example.myplaylistmaker.search.data.network.RetrofitNetworkClient
import com.example.myplaylistmaker.search.data.network.TracksRepositoryImpl
import com.example.myplaylistmaker.search.data.sharedprefs.SharedPrefsImpl
import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.api.TracksInteractor
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.impl.TracksInteractorImpl
import com.example.myplaylistmaker.settings.data.SettingsSharedPrefsImpl
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs


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


}