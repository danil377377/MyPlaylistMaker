package com.example.myplaylistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val songIcon: ImageView = itemView.findViewById(R.id.song_icon)
    private val songName: TextView = itemView.findViewById(R.id.song_name)
    private val singerName: TextView = itemView.findViewById(R.id.singer_name)

    fun bind(model: Track) {
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.placeholder).into(songIcon)

        songName.text = model.trackName
        singerName.text = "${model.artistName} • ${model.trackTime}"


    }

}