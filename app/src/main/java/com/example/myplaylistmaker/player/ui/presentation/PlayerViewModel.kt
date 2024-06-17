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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myplaylistmaker.player.domain.GlideLoader
import com.example.myplaylistmaker.player.ui.models.PlayerState
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.utility.App
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(application: Application,
    val glideLoader : GlideLoader,
    val favoritesInteractor: FavoritesInteractor
) : AndroidViewModel(application) {
     var time = ""
    private var mediaPlayer: MediaPlayer = MediaPlayer()
lateinit var track:Track
    private var timerJob: Job? = null
    private var favoriteLivaeData = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = favoriteLivaeData

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

        startTimer()
        renderState(PlayerState.Play())
    }

    fun pausePlayback() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED

        renderState(PlayerState.Pause())
    }

    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPrepared()

        }
        mediaPlayer.setOnCompletionListener {
            onComplete()

            mediaPlayer.pause()
            playerState = STATE_PAUSED
            renderState(PlayerState.Pause())
        }

    }

    private fun renderState(state: PlayerState) {
        playLiveData.postValue(state)
    }
    override fun onCleared() {
        pausePlayback()
        mediaPlayer.release()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                time = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            }
        }
    }
    fun onFavoriteClicked() {
        if (track != null) {
            viewModelScope.launch {
                if (track.isFavorite) {
                    favoritesInteractor.deleteTrackFromFavorites(track)
                    track.isFavorite = !track.isFavorite
                } else {
                    favoritesInteractor.addTrackToFavorites(track)
                    track.isFavorite = !track.isFavorite
                }

                favoriteLivaeData.postValue(track.isFavorite)
            }
        }
    }
}











