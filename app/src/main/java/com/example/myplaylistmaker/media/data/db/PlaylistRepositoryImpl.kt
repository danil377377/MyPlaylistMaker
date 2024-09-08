package com.example.myplaylistmaker.media.data.db

import com.example.myplaylistmaker.db.AppDatabase
import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.data.converters.PlaylistDbConvertor
import com.example.myplaylistmaker.media.domain.db.PlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,

    ) : PlaylistRepository {
    override suspend fun getPlaylist(id: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(id)
        emit(convertFromPlaylistEntity(playlist!!))
    }

    override suspend fun getAllTracksFromPlaylists(id: Int): Flow<List<Track>> {
        val favoritesIds = withContext(Dispatchers.IO) {
            appDatabase.favoriteTracksDao().getTracksId().first()
        }
        return appDatabase.TrackInPlaylistEntityDao().getTracks().map { tracks ->
            tracks.map { track ->
                Track(
                    track.id.toInt(),
                    track.trackName,
                    track.artistName,
                    track.trackTimeMillis,
                    track.artworkUrl100,
                    track.collectionName,
                    track.releaseDate,
                    track.collectionName,
                    track.country,
                    track.artworkUrl100,
                    track.coverArtWork,
                    favoritesIds.contains(track.id)
                )
            }
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists).reversed())
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistDb = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistDb)

    }

    override suspend fun deletePlaylistbyId(playlistId: Int) {
        appDatabase.playlistDao().deletePlaylistbyId(playlistId)
    }

    override suspend fun updatePlaylistById(playlistId: Int, name:String,description:String, pathToFIle:String?) {
        appDatabase.playlistDao().updatePlaylistById(playlistId,name,description,pathToFIle)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistDbConvertor.map(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        appDatabase.playlistDao().addTrackToPlaylist(playlist.id, track.trackId.toString())
        addTrackToTrackInPlaylist(track)
    }

    override suspend fun addTrackToTrackInPlaylist(track: Track) {
        appDatabase.TrackInPlaylistEntityDao().insertTrack(
            TrackInPlaylistEntity(
                track.trackId.toString(),
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.collectionName,
                track.country,
                track.artworkUrl100,
                track.coverArtWork,
                "trackFile"
            )
        )
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val playlists = appDatabase.playlistDao().getPlaylists()
        var containingInPlaylists = 0
        playlists.forEach {
            if (it.tracksIds.split(",").map { it.trim() }
                    .contains(trackId.toString())) containingInPlaylists++
        }
        if (containingInPlaylists <= 1) {
            appDatabase.TrackInPlaylistEntityDao().deleteTrackById(trackId)
        }
        if (playlist != null) {
            val trackIdsList: MutableList<String> =
                playlist.tracksIds.split(",").map { it.trim() }.toMutableList()
            if (trackIdsList.contains(trackId.toString())) {
                trackIdsList.remove(trackId.toString())
                val changedPlaylist = PlaylistEntity(
                    playlist.id,
                    playlist.name,
                    playlist.description,
                    playlist.pathToFile,
                    trackIdsList.joinToString(","),
                    playlist.quantityTracks - 1
                )

                appDatabase.playlistDao().updatePlaylist(changedPlaylist)

            }
        }
    }


}