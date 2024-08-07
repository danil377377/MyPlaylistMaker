package com.example.myplaylistmaker.di

import android.app.Application
import android.media.MediaPlayer
import androidx.room.Room
import com.example.myplaylistmaker.db.AppDatabase
import com.example.myplaylistmaker.media.data.ImageDecoderImpl
import com.example.myplaylistmaker.media.data.ImageStorageImpl
import com.example.myplaylistmaker.media.data.converters.PlaylistDbConvertor
import com.example.myplaylistmaker.media.data.db.MakePlaylistRepositoryImpl
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.ImageStorage
import com.example.myplaylistmaker.media.domain.db.MakePlaylistRepository
import com.example.myplaylistmaker.player.data.GlideLoaderImpl
import com.example.myplaylistmaker.player.data.MediaPlayerWrapperImpl
import com.example.myplaylistmaker.player.domain.GlideLoader
import com.example.myplaylistmaker.player.domain.MediaPlayerWrapper
import com.example.myplaylistmaker.search.data.converters.TrackDbConvertor
import com.example.myplaylistmaker.search.data.db.FavoritesRepositoryImpl
import com.example.myplaylistmaker.search.data.network.ITunesApi
import com.example.myplaylistmaker.search.data.network.NetworkClient
import com.example.myplaylistmaker.search.data.network.RetrofitNetworkClient
import com.example.myplaylistmaker.search.data.network.TracksRepositoryImpl
import com.example.myplaylistmaker.search.data.sharedprefs.SharedPrefsImpl
import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.db.FavoritesRepository
import com.example.myplaylistmaker.settings.data.SettingsSharedPrefsImpl
import com.example.myplaylistmaker.settings.domen.SettingsSharedPrefs
import com.example.myplaylistmaker.sharing.domen.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        PlaylistDbConvertor()
    }
}

val repositoryModule = module {
    factory { TrackDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
    single<MakePlaylistRepository> { MakePlaylistRepositoryImpl(get(), get()) }

    factory<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }


    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }
    factory<SharedPrefs> {
        SharedPrefsImpl(get(), get(), get())
    }
    factory { Gson() }
    factory { MediaPlayer() }
    factory<GlideLoader> {
        GlideLoaderImpl(get())
    }
    factory<ImageStorage> {
        ImageStorageImpl(androidContext())
    }
    factory<ImageDecoder> {
        ImageDecoderImpl()
    }
    factory<SettingsSharedPrefs> {
        SettingsSharedPrefsImpl(get())
    }
    factory<MediaPlayerWrapper> {
        MediaPlayerWrapperImpl(get())
    }
    single {
        androidContext()
            .getSharedPreferences("practicum_example_preferences", Application.MODE_PRIVATE)
    }

    factory {
        ExternalNavigator()
    }


}