package com.example.myplaylistmaker.media.domain.impl

import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.db.MakePlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class MakePlaylistInteractorImpl(private val makePlaylistRepository: MakePlaylistRepository):
    MakePlaylistInteractor {


    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return  makePlaylistRepository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        makePlaylistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        makePlaylistRepository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track) {
        makePlaylistRepository.addTrackToPlaylist(playlistId, track)
    }


}