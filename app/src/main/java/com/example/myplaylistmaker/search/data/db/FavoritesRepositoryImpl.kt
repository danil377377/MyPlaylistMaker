package com.example.myplaylistmaker.search.data.db

import com.example.myplaylistmaker.db.AppDatabase
import com.example.myplaylistmaker.db.entity.TrackEntity
import com.example.myplaylistmaker.search.data.converters.TrackDbConvertor
import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.db.FavoritesRepository
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackDto = convertFromTrackToTrackDto(track)
        val trackDb = trackDbConvertor.map(trackDto)
        appDatabase.trackDao().insertTrack(trackDb)

    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        val trackDto = convertFromTrackToTrackDto(track)
        val trackDb = trackDbConvertor.map(trackDto)
        appDatabase.trackDao().deleteTrack(trackDb)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    private fun convertFromTrackToTrackDto(track: Track): TrackDto{
        return TrackDto(track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100!!,
            track.collectionName!!,
            track.releaseDate!!,
            track.primaryGenreName!!,
            track.country!!,
            track.previewUrl!!,
        )
    }
}