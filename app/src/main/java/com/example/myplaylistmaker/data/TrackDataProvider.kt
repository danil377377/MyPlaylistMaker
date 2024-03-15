package com.example.myplaylistmaker.data

import com.example.myplaylistmaker.models.Track

interface TrackDataProvider {
    fun provideTrackFromJson(track: String): Track
}