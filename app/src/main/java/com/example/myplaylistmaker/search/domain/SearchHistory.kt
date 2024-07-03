package com.example.myplaylistmaker.search.domain

import android.content.Context

import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



class SearchHistory(): KoinComponent {
    private val sharedPrefs: SharedPrefs by inject()
    private var historyTrackList = arrayListOf<Track>()

    init {

        CoroutineScope(Dispatchers.IO).launch {
            historyTrackList = getHistoryTrackList()
        }
    }
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

     suspend fun getHistoryTrackList(): ArrayList<Track> {
        return sharedPrefs.getHistory()
    }
}