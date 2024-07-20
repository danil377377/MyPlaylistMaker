package com.example.myplaylistmaker.media.data.db

import com.example.myplaylistmaker.db.AppDatabase
import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.db.entity.TrackEntity
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.data.converters.PlaylistDbConvertor
import com.example.myplaylistmaker.media.domain.db.MakePlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.data.converters.TrackDbConvertor
import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.db.FavoritesRepository
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MakePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor

) : MakePlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists).reversed())
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistDb = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistDb)

    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistDb = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().deletePlaylist(playlistDb)
    }
    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        appDatabase.playlistDao().addTrackToPlaylist(playlist.id, track.trackId.toString())
        addTrackToTrackInPlaylist(track)
    }

    override suspend fun addTrackToTrackInPlaylist(track: Track) {
        appDatabase.TrackInPlaylistEntityDao().insertTrack(TrackInPlaylistEntity(track.trackId.toString(), track.trackName, track.artistName,track.trackTimeMillis, track.artworkUrl100, track.collectionName, track.releaseDate, track.collectionName, track.country, track.artworkUrl100, track.coverArtWork,"trackFile"))
    }


}