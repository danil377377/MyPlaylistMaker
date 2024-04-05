package com.example.myplaylistmaker.search.domain

import android.content.Context
import com.example.imdbtraining.utility.Creator.provideSharedPrefs
import com.example.myplaylistmaker.search.domain.models.Track


class SearchHistory(context: Context) {
    private val sharedPrefs = provideSharedPrefs(context)
    private var historyTrackList = getHistoryTrackList()
    fun clearHistory(){
        sharedPrefs.clearHistorySharedPrefs()
        historyTrackList.clear()
    }
    fun addToHistory(track: Track) {

        val existingTrackIndex = historyTrackList.indexOfFirst { it.trackId == track.trackId }

        if (existingTrackIndex != -1) {

            historyTrackList.removeAt(existingTrackIndex)
        } else if (historyTrackList.size >= 10) {

            historyTrackList.removeLast()
        }

        historyTrackList.add(0, track)


        sharedPrefs.addToHistorySharedPrefs(historyTrackList)

    }

    fun getHistoryTrackList(): ArrayList<Track> {
        return sharedPrefs.getHistory()
    }
}