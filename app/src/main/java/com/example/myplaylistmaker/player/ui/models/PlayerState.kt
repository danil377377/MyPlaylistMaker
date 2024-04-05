package com.example.myplaylistmaker.player.ui.models

import com.example.myplaylistmaker.search.domain.models.Track

sealed interface PlayerState {



     class Play(

    ) : PlayerState

    class Pause: PlayerState

    class Prepare: PlayerState

}