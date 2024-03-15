package com.example.myplaylistmaker.presentation

import com.example.myplaylistmaker.models.Track

interface TrackDataProvider {
    fun provideTrackFromJson(track: String): Track
}