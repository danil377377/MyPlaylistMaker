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
        emit(convertFromMovieEntity(movies))
    }

    override suspend fun addTrackToFavorites(track: TrackDto) {
        val trackDb = movieDbConvertor.map(track)
        appDatabase.trackDao().insertTrack(trackDb)
    }

    override fun deleteTrackFromFavorites(track: TrackDto) {
        val trackDb = movieDbConvertor.map(track)
        appDatabase.trackDao().deleteTrack(trackDb)
    }

    private fun convertFromMovieEntity(movies: List<TrackEntity>): List<Track> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }
}