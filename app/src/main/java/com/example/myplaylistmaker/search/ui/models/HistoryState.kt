package com.example.myplaylistmaker.search.ui.models

import com.example.myplaylistmaker.search.domain.models.Track


    sealed interface HistoryState {



        data class Content(
            val tracks: List<Track>
        ) : HistoryState


         class Empty: HistoryState

    }
