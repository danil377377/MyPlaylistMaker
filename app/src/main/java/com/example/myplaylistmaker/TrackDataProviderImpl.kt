package com.example.myplaylistmaker

import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.presentation.TrackDataProvider
import com.google.gson.Gson

class TrackDataProviderImpl: TrackDataProvider {
    override fun provideTrackFromJson(track: String): Track {
        val gson = Gson()
        return gson.fromJson(track, Track::class.java)
    }
}