package com.example.myplaylistmaker.player.data

import android.media.MediaPlayer

class MediaPlayerWrapper(
    private val mediaPlayer: MediaPlayer = MediaPlayer()
) {

    private var onCompletionListener: (() -> Unit)? = null
    private var onPreparedListener: (() -> Unit)? = null

    init {
        mediaPlayer.setOnCompletionListener {
            onCompletionListener?.invoke()
        }
        mediaPlayer.setOnPreparedListener {
            onPreparedListener?.invoke()
        }
    }

    fun setDataSource(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }

    fun setOnPreparedListener(listener: () -> Unit) {
        onPreparedListener = listener
    }
}