package com.example.myplaylistmaker.search.ui.models

import com.example.myplaylistmaker.search.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

    data class Error(
        val errorMessage: String
    ) : TracksState

    data class Empty(
        val message: String
    ) : TracksState

}