package com.example.myplaylistmaker.media.domain.db


import com.example.myplaylistmaker.db.dao.TrackInPlaylistEntityDao
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylist(id: Int): Flow<Playlist>
    suspend fun getAllTracksFromPlaylists(id: Int): Flow<List<Track>>
    suspend fun getTracksFromPlaylist(id: Int): Flow<List<Track>>

    suspend fun getPlaylists(): Flow<List<Playlist>>


    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylistById(playlistId: Int)
    suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track)
    fun saveImageToPrivateStorage(uri: String, name: String): String?
    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)

}