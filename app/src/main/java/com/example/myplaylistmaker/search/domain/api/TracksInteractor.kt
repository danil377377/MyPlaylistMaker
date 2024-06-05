package com.example.myplaylistmaker.search.domain.api

import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>


}
