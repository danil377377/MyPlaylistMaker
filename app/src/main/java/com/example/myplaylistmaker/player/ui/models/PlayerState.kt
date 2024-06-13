package com.example.myplaylistmaker.player.ui.models

sealed interface PlayerState {
    class Play(

    ) : PlayerState

    class Pause: PlayerState

    class Prepare: PlayerState

}