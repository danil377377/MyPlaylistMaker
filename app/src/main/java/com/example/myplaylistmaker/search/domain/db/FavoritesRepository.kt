package com.example.myplaylistmaker.search.domain.db

import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritesTracks(): Flow<List<Track>>
    suspend fun addTrackToFavorites(track: Track)
    suspend fun deleteTrackFromFavorites(track: Track)
}