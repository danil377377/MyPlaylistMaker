package com.example.myplaylistmaker.presentation

import com.example.myplaylistmaker.search.domain.models.Track

interface TrackDataProvider {
    fun provideTrackFromJson(track: String): Track
}