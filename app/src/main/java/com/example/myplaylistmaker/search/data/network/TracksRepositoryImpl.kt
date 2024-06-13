package com.example.myplaylistmaker.search.data.network

import com.example.imdbtraining.utility.Resource
import com.example.myplaylistmaker.search.data.dto.ITunesRequest
import com.example.myplaylistmaker.search.data.dto.ITunesResponse
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(ITunesRequest(expression))
         when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                emit(Resource.Success((response as ITunesResponse).results.map {
                    Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate,
                        it.primaryGenreName, it.country, it.previewUrl, it.getCoverArtwork())}))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}