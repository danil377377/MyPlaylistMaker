package com.example.myplaylistmaker.player.ui.presentation

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.imdbtraining.utility.Creator
import com.example.myplaylistmaker.player.ui.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val glideLoader = Creator.provideGlideLoader(application)

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
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
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
        // Здесь вызовите методы MediaPlayer для начала воспроизведения
        mediaPlayer.start()
//        pause.visibility = View.VISIBLE
//        pause.isEnabled = true
//        play.visibility = View.INVISIBLE
//        play.isEnabled=false
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            updateTimeRunnable
        )
        renderState(PlayerState.Play())
    }

    fun pausePlayback() {
        // Здесь вызовите методы MediaPlayer для паузы воспроизведения
        mediaPlayer.pause()
//        play.visibility = View.VISIBLE
//        play.isEnabled = true
//        pause.visibility = View.INVISIBLE
//        pause.isEnabled=false
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
        renderState(PlayerState.Pause())
    }

    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplete: () -> Unit) {
        // Здесь вызовите методы MediaPlayer для подготовки к воспроизведению трека по переданному URL
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
//            play.isEnabled = true
            playerState = STATE_PREPARED
            onPrepared()

        }
        mediaPlayer.setOnCompletionListener {
//            play.visibility = View.VISIBLE
//            pause.visibility = View.INVISIBLE
//            pause.isEnabled=false
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











