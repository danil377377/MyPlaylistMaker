package com.example.myplaylistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistsBottomSheetViewHolder(view: View, private val imageDecoder: ImageDecoder): RecyclerView.ViewHolder(view) {

    private val name: TextView = itemView.findViewById(R.id.playlist_name)

    private val image: ImageView = itemView.findViewById(R.id.playlist_image)
    private val quantity: TextView = itemView.findViewById(R.id.playlist_quantity)

    fun bind(playlist: Playlist) {
        name.text = playlist.name

        if(playlist.pathToFile!=null) image.setImageBitmap(playlist.getImage(imageDecoder))
        quantity.text = getTrackCountString(playlist.quantityTracks)
    }

    fun getTrackCountString(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "$count треков"
            lastDigit == 1 -> "$count трек"
            lastDigit in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }
}