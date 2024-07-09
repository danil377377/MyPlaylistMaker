package com.example.myplaylistmaker.media.domain.db

import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MakePlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track)
}