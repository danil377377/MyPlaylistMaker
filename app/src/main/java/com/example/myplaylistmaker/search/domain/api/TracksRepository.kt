package com.example.myplaylistmaker.search.domain.api

import com.example.imdbtraining.utility.Resource
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}