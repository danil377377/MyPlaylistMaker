package com.example.myplaylistmaker.search.data.network

import com.example.imdbtraining.utility.Resource
import com.example.myplaylistmaker.search.data.dto.ITunesRequest
import com.example.myplaylistmaker.search.data.dto.ITunesResponse
import com.example.myplaylistmaker.search.domain.api.TracksRepository
import com.example.myplaylistmaker.search.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(ITunesRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as ITunesResponse).results.map {
                    Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate,
                        it.primaryGenreName, it.country, it.previewUrl, it.getCoverArtwork())})
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}