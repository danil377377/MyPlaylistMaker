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
    private val movieDbConvertor: TrackDbConvertor,
) : FavoritesRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val movies = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(movies))
    }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackDto = convertFromTrackToTrackDto(track)
        val trackDb = movieDbConvertor.map(trackDto)
        appDatabase.trackDao().insertTrack(trackDb)
    }

    override fun deleteTrackFromFavorites(track: Track) {
        val trackDto = convertFromTrackToTrackDto(track)
        val trackDb = movieDbConvertor.map(trackDto)
        appDatabase.trackDao().deleteTrack(trackDb)
    }

    private fun convertFromTrackEntity(movies: List<TrackEntity>): List<Track> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
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