package com.example.myplaylistmaker.media.domain.impl

import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.domain.ImageStorage
import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.db.PlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository, private val imageStorage: ImageStorage):
    PlaylistInteractor {
     override fun saveImageToPrivateStorage(uri: String, name: String): String? {
        return imageStorage.saveImage(uri, name)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override suspend fun getAllTracksFromPlaylists(id: Int): Flow<List<Track>> {
        return playlistRepository.getAllTracksFromPlaylists(id)
    }

    override suspend fun getTracksFromPlaylist(id: Int): Flow<List<Track>> {
        val playlist = getPlaylist(id).first()
        return getAllTracksFromPlaylists(id).map { tracks -> tracks.filter { playlist.tracksIds.contains(it.trackId.toString()) }   }
    }


    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return  playlistRepository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        playlistRepository.deletePlaylistbyId(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlistId, track)
    }


}