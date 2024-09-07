package com.example.myplaylistmaker.media.domain.db

import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylist(id: Int): Flow<Playlist>
     suspend fun getAllTracksFromPlaylists(id: Int): Flow<List<Track>>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

    suspend fun addTrackToTrackInPlaylist(track: Track)
    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)
}