package com.example.myplaylistmaker.search.domain.api

import com.example.myplaylistmaker.search.domain.models.Track


interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundMovies: List<Track>?, errorMessage: String?)
    }
}
