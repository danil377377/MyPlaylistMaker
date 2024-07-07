package com.example.myplaylistmaker.player.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.main.ui.RootActivity
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.player.ui.models.PlayerState
import com.example.myplaylistmaker.player.ui.presentation.PlayerViewModel
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private lateinit var play: ImageView
    private lateinit var pause: ImageView
    private lateinit var time: TextView
    private lateinit var url: String
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var addtoFavorites :ImageView
    private lateinit var deleteFromVavorites :ImageView

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MyTest", "onCreatePlayer")
        setContentView(R.layout.player)
        Log.d("MyTest", "onCreatePlayerSetContenrView")
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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val newPlaylistButton = findViewById<Button>(R.id.newPlaylistButton)


        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PlaylistsBottomSheetAdapter(emptyList()){

        }
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.getListOfPlaylists()}
        viewModel.observePlaylists().observe(this){
            if(it != emptyList<Playlist>()) {
                recyclerView.isVisible = true
                adapter.clear()
                adapter.addAll(it)
                adapter.notifyDataSetChanged()
            }
            else{
                recyclerView.isVisible = false
            }
        }


        if (track != null) {
            viewModel.mysetTrack(track)
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
        addtoFavorites = findViewById<ImageView>(R.id.AddtoFavorites)
        deleteFromVavorites = findViewById<ImageView>(R.id.DeleteFromFavorites)
        deleteFromVavorites.setOnClickListener{
            viewModel.onFavoriteClicked()
        }
        addtoFavorites.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        newPlaylistButton.setOnClickListener{
            val intent: Intent = Intent(
                this@PlayerActivity,
                RootActivity::class.java
            )
            intent.putExtra("showFragment", true)
            startActivity(intent)
        }

        if (track != null) {


            url = track.previewUrl ?: ""

            viewModel.loadIcon(this, track.coverArtWork ?: "", icon)
            Log.d("MyTest", "viewModelLoadIcon")
            songName.text = track.trackName
            singerName.text = track.artistName
            fullDurability.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            year.text = track.releaseDate?.substring(0, 4)

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
        Log.d("MyTest", "viewModelPreparePlayer")
        viewModel.observeFavorite().observe(this){
            renderFavorites(it)

        }

        viewModel.observePlay().observe(this) {
            render(it)
        }

        play.setOnClickListener {
            playbackControl()
        }
        pause.setOnClickListener {
            playbackControl()
        }


    }


    override fun onPause() {
        super.onPause()
        render(PlayerState.Pause())

    }

    fun renderFavorites(value: Boolean){
        if (value) {
            addtoFavorites.isVisible = false
            deleteFromVavorites.isVisible = true
            deleteFromVavorites.isEnabled = true
        } else {
            addtoFavorites.isVisible = true
            deleteFromVavorites.isVisible = false
            addtoFavorites.isEnabled = true
        }
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
