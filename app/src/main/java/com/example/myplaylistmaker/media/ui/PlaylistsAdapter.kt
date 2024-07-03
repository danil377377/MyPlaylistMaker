package com.example.myplaylistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistsAdapter(private val playlists: List<Playlist>): RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_card, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}