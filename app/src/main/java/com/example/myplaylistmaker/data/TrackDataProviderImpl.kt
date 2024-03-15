package com.example.myplaylistmaker.data

import com.example.myplaylistmaker.models.Track
import com.google.gson.Gson

class TrackDataProviderImpl:TrackDataProvider {
    override fun provideTrackFromJson(track: String): Track {
        val gson = Gson()
        return gson.fromJson(track, Track::class.java)
    }
}