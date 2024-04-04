package com.example.myplaylistmaker.search.domain.api

import com.example.imdbtraining.utility.Resource
import com.example.myplaylistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}