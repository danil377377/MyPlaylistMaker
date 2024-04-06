package com.example.myplaylistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.search.domain.models.Track

class TrackAdapter( val clickListener: LocationClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { clickListener.onLocationClick(trackList.get(position)) }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
    fun interface LocationClickListener {
        fun onLocationClick(location: Track)
    }
    fun updateTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }

}
