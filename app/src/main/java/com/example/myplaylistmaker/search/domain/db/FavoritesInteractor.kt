package com.example.myplaylistmaker.search.domain.db

import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun addTrackToFavorites(track: Track)
    fun deleteTrackFromFavorites(track: Track)
}