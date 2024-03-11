package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null
    private lateinit var play: ImageView
    private lateinit var pause: ImageView
    private lateinit var time: TextView
    private var mediaPlayer = MediaPlayer()
    private var url: String? = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    val updateTimeRunnable = object : Runnable {
        override fun run() {
            time.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            mainThreadHandler?.postDelayed(this, 300)
        }
    }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        val track = intent.getStringExtra("track")?.let { createTrackFromJson(it) }
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





        backButton.setOnClickListener {
            onBackPressed()
        }
        val radiusInDp = 8 // радиус скругления в dp
        val radiusInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radiusInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        if (track != null) {
            url = track.previewUrl
            Glide.with(icon).load(track.getCoverArtwork()).transform(RoundedCorners(radiusInPixels))
                .placeholder(R.drawable.placeholder)
                .into(icon)
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

        mainThreadHandler = Handler(Looper.getMainLooper())
        preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }
        pause.setOnClickListener {
            playbackControl()
        }

        if (playerState == STATE_PLAYING) mainThreadHandler?.post(
            updateTimeRunnable
        )
        if (playerState == STATE_PAUSED) mainThreadHandler?.removeCallbacks(updateTimeRunnable)

        mediaPlayer.setOnCompletionListener {
            time.text = "00:00"
            pausePlayer()
            mainThreadHandler?.removeCallbacks(updateTimeRunnable)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        pausePlayer()
        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
        mediaPlayer.release()

    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun createTrackFromJson(track: String): Track {
        val gson = Gson()
        return gson.fromJson(track, Track::class.java)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {

        mediaPlayer.start()
        pause.visibility = View.VISIBLE
        pause.isEnabled = true
        play.visibility = View.INVISIBLE
        play.isEnabled=false
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            updateTimeRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.visibility = View.VISIBLE
        play.isEnabled = true
        pause.visibility = View.INVISIBLE
        pause.isEnabled=false
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(updateTimeRunnable)
    }

    private fun preparePlayer() {

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.visibility = View.VISIBLE
            pause.visibility = View.INVISIBLE
            pause.isEnabled=false
            playerState = STATE_PREPARED

        }

    }
}
