package com.example.myplaylistmaker.player.data

import android.media.MediaPlayer
import com.example.myplaylistmaker.player.domain.MediaPlayerWrapper

class MediaPlayerWrapperImpl(
    private val mediaPlayer: MediaPlayer
): MediaPlayerWrapper {

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

    override fun setDataSource(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        onPreparedListener = listener
    }
}