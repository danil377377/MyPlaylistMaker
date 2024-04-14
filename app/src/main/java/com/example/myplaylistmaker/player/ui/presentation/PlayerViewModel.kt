package com.example.myplaylistmaker.player.ui.presentation

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myplaylistmaker.player.domain.GlideLoader
import com.example.myplaylistmaker.player.ui.models.PlayerState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    application: Application,
) : AndroidViewModel(application), KoinComponent {
    private val glideLoader : GlideLoader by inject()

     var time = ""
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    val updateTimeRunnable = object : Runnable {
        override fun run() {
            time = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(this, 300)
        }
    }
    val mainThreadHandler = Handler(Looper.getMainLooper())

    private val playLiveData = MutableLiveData<PlayerState>()
    fun observePlay(): LiveData<PlayerState> = playLiveData

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

    }

    private var playerState = STATE_DEFAULT
    fun loadIcon(context: Context, url: String, imageView: ImageView) {
        glideLoader.loadRoundedImage(context, url, imageView)

    }

    fun playbackControl() {
        when (playerState) {

            STATE_PLAYING -> {
                pausePlayback()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayback()
            }
        }
    }

    fun startPlayback() {
        mediaPlayer.start()

        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            updateTimeRunnable
        )
        renderState(PlayerState.Play())
    }

    fun pausePlayback() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
        renderState(PlayerState.Pause())
    }

    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onComplete()
            mainThreadHandler?.removeCallbacks(updateTimeRunnable)
            mediaPlayer.pause() // Пауза после завершения трека
            playerState = STATE_PAUSED // Установка состояния в STATE_PAUSED
            renderState(PlayerState.Pause()) // Обновление LiveData
        }
        renderState(PlayerState.Prepare())
    }

    private fun renderState(state: PlayerState) {
        playLiveData.postValue(state)
    }
    override fun onCleared() {
        pausePlayback()
        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
        mediaPlayer.release()
    }
}











