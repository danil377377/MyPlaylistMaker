package com.example.myplaylistmaker.player.ui
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.player.ui.models.PlayerState
import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.utility.App
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var play: ImageView
    private lateinit var pause: ImageView
    private lateinit var time: TextView
    private lateinit var url: String
    private lateinit var viewModel: PlayerViewModel
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
        val view: PlayerViewModel by viewModel()
        viewModel = view

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

}
