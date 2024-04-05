package com.example.myplaylistmaker.player.ui

import android.annotation.SuppressLint

import android.os.Bundle
import android.os.Handler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.player.ui.models.PlayerState

import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : ComponentActivity() {
//    companion object {
//        const val STATE_DEFAULT = 0
//        private const val STATE_PREPARED = 1
//        private const val STATE_PLAYING = 2
//        private const val STATE_PAUSED = 3
//    }

    //    private var playerState = STATE_DEFAULT
//    private var mainThreadHandler: Handler? = null
    private lateinit var play: ImageView
    private lateinit var pause: ImageView
    private lateinit var time: TextView

    //    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private lateinit var viewModel: PlayerViewModel
//    val updateTimeRunnable = object : Runnable {
//        override fun run() {
//            time.text = SimpleDateFormat(
//                "mm:ss",
//                Locale.getDefault()
//            ).format(mediaPlayer.currentPosition)
//            mainThreadHandler?.postDelayed(this, 300)
//        }
//    }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        val track = intent.getSerializableExtra("track") as? Track
        val backButton = findViewById<ImageView>(R.id.backButton)
        val icon = findViewById<ImageView>(R.id.icon)
        val songName = findViewById<TextView>(R.id.songName)
        val singerName = findViewById<TextView>(R.id.singerName)
        val fullDurability = findViewById<TextView>(R.id.Fulldurability)
        val year = findViewById<TextView>(R.id.year)
        val album = findViewById<TextView>(R.id.album)
        val albumInfo = findViewById<TextView>(R.id.albumInfo)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]





        backButton.setOnClickListener {
            onBackPressed()
        }
        if (track != null) {
            url = track.previewUrl

            viewModel.loadIcon(this, track.coverArtWork, icon)

            songName.text = track.trackName
            singerName.text = track.artistName
            fullDurability.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            year.text = track.releaseDate.substring(0, 4)

            if (track.collectionName != null || track.collectionName != "") {
                album.text = track.collectionName
                album.visibility = View.VISIBLE
                albumInfo.visibility = View.VISIBLE
            }
            genre.text = track.primaryGenreName
            country.text = track.country
        }



        play = findViewById(R.id.Play)
        pause = findViewById<ImageView?>(R.id.Pause)
        time = findViewById(R.id.durability)

//        mainThreadHandler = Handler(Looper.getMainLooper())
//        preparePlayer()

        viewModel.preparePlayer(url,
            onPrepared = {
                play.isEnabled = true
            },
            onComplete = {
                time.text = "00:00"


                play.visibility = View.VISIBLE
                pause.visibility = View.INVISIBLE
                pause.isEnabled = false
            })


        play.setOnClickListener {
            playbackControl()
        }
        pause.setOnClickListener {
            playbackControl()
        }





        viewModel.observePlay().observe(this) {
            render(it)
        }
    }



    override fun onPause() {
        super.onPause()
        render(PlayerState.Pause())
    }


    fun render(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Pause -> {
                viewModel.pausePlayback()
                play.visibility = View.VISIBLE
                play.isEnabled = true
                pause.visibility = View.INVISIBLE
                pause.isEnabled = false
            }

            is PlayerState.Play -> {
                viewModel.startPlayback()
                pause.visibility = View.VISIBLE
                pause.isEnabled = true
                play.visibility = View.INVISIBLE
                play.isEnabled = false
                time.text = viewModel.time
            }

            is PlayerState.Prepare -> {
                viewModel.preparePlayer(url,
                    onPrepared = {
                        play.isEnabled = true
                    },
                    onComplete = {
                        time.text = "00:00"


                        play.visibility = View.VISIBLE
                        pause.visibility = View.INVISIBLE
                        pause.isEnabled = false
                    })

            }
        }
    }

    private fun playbackControl() {
       viewModel.playbackControl()
    }

//    private fun startPlayer() {
//
//        mediaPlayer.start()
//        pause.visibility = View.VISIBLE
//        pause.isEnabled = true
//        play.visibility = View.INVISIBLE
//        play.isEnabled = false
//        playerState = STATE_PLAYING
//        mainThreadHandler?.post(
//            updateTimeRunnable
//        )
//    }
//
//    private fun pausePlayer() {
//        mediaPlayer.pause()
//
//        playerState = STATE_PAUSED
//        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
//    }
//
//    private fun preparePlayer() {
//
//        mediaPlayer.setDataSource(url)
//        mediaPlayer.prepareAsync()
//        mediaPlayer.setOnPreparedListener {
//            play.isEnabled = true
//            playerState = STATE_PREPARED
//        }
//        mediaPlayer.setOnCompletionListener {
//            play.visibility = View.VISIBLE
//            pause.visibility = View.INVISIBLE
//            pause.isEnabled = false
//            playerState = STATE_PREPARED
//
//        }
//
//    }
}
