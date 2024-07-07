package com.example.myplaylistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistsBottomSheetViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val name: TextView = itemView.findViewById(R.id.playlist_name)

    private val image: ImageView = itemView.findViewById(R.id.playlist_image)
    private val quantity: TextView = itemView.findViewById(R.id.playlist_quantity)

    fun bind(playlist: Playlist) {
        name.text = playlist.name

        if(playlist.pathToFile!=null) image.setImageBitmap(playlist.getImage())
        quantity.text = playlist.quantityTracks.toString()
    }
}