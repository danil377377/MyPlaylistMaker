package com.example.myplaylistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import com.example.myplaylistmaker.search.ui.TrackAdapter

class PlaylistsAdapter(private var playlists: List<Playlist>, val clickListener: PlaylistsAdapter.LocationClickListener): RecyclerView.Adapter<PlaylistsViewHolder>() {
    fun clear(){
        playlists = emptyList()
    }
    fun addAll(newPlaylists: List<Playlist>){
        playlists = newPlaylists
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_card, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onLocationClick(playlists[position]) }
    }
    fun interface LocationClickListener {
        fun onLocationClick(location: Playlist)
    }
}