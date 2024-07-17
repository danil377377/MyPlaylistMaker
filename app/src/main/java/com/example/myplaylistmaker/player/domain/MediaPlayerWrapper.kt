package com.example.myplaylistmaker.player.domain

interface MediaPlayerWrapper {
    fun setDataSource(url: String)
    fun start()
    fun pause()
    fun release()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun setOnPreparedListener(listener: () -> Unit)
}