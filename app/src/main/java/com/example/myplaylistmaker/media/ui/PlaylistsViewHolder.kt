package com.example.myplaylistmaker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.utility.StringUtils

class PlaylistsViewHolder(view: View,private val imageDecoder: ImageDecoder): RecyclerView.ViewHolder(view) {

    private val name: TextView = itemView.findViewById(R.id.playlist_name)

    private val image: ImageView = itemView.findViewById(R.id.playlist_image)
    private val quantity: TextView = itemView.findViewById(R.id.playlist_quantity)

    fun bind(playlist: Playlist) {
        name.text = playlist.name

       if(playlist.pathToFile!=null) image.setImageBitmap(playlist.getImage(imageDecoder)) else image.setImageResource(R.drawable.placeholder)
        quantity.text = StringUtils.getTrackCountString(playlist.quantityTracks)
    }

}