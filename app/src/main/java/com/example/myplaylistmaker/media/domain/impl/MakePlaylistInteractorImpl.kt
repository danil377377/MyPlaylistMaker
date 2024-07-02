package com.example.myplaylistmaker.media.domain.impl

import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.db.MakePlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.db.FavoritesRepository
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MakePlaylistInteractorImpl(private val makePlaylistRepository: MakePlaylistRepository):
    MakePlaylistInteractor {
//    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
//        return favoritesRepository.getFavoritesTracks().map{ tracks ->
//            tracks.reversed()
//        }
//    }
//
//    override suspend fun addTrackToFavorites(track: Track) {
//        favoritesRepository.addTrackToFavorites(track)
//    }
//
//    override suspend fun deleteTrackFromFavorites(track: Track) {
//        favoritesRepository.deleteTrackFromFavorites(track)
//    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return  makePlaylistRepository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        makePlaylistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        makePlaylistRepository.deletePlaylist(playlist)
    }
}