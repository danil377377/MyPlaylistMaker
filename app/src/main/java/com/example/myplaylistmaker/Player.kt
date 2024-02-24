package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class Player : AppCompatActivity() {
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
        if(track != null) {
            Glide.with(icon).load(track.getCoverArtwork()).transform(RoundedCorners(radiusInPixels)).placeholder(R.drawable.placeholder)
                .into(icon)
            songName.text = track.trackName
            singerName.text = track.artistName
            fullDurability.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            year.text = track.releaseDate.substring(0,4)

            if (track.collectionName != null || track.collectionName!= ""){album.text = track.collectionName
                album.visibility = View.VISIBLE
                albumInfo.visibility = View.VISIBLE}
            genre.text = track.primaryGenreName
            country.text = track.country
        }
    }
    private fun createTrackFromJson(track: String): Track {
         val gson = Gson()
            return gson.fromJson(track,Track::class.java)
    }
}
