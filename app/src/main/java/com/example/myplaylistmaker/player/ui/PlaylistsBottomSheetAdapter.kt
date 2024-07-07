package com.example.myplaylistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistsBottomSheetAdapter(private var playlists: List<Playlist>, val clickListener: PlaylistsBottomSheetAdapter.LocationClickListener): RecyclerView.Adapter<PlaylistsBottomSheetViewHolder>() {
    fun clear(){
        playlists = emptyList()
    }
    fun addAll(newPlaylists: List<Playlist>){
        playlists = newPlaylists
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_card_for_bottom_sheet, parent, false)
        return PlaylistsBottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onLocationClick(playlists[position]) }
    }
    fun interface LocationClickListener {
        fun onLocationClick(location: Playlist)
    }
}