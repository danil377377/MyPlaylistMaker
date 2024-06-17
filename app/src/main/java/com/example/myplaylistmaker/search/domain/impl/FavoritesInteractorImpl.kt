package com.example.myplaylistmaker.search.domain.impl

import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.db.FavoritesRepository
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {
    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesTracks().map{ tracks ->
            tracks.reversed()
        }
    }

    override suspend fun addTrackToFavorites(track: Track) {
        favoritesRepository.addTrackToFavorites(track)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        favoritesRepository.deleteTrackFromFavorites(track)
    }
}