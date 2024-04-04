package com.example.myplaylistmaker

import android.content.SharedPreferences
import com.example.myplaylistmaker.search.ui.TRACK_HISTORY_KEY
import com.example.myplaylistmaker.search.domain.models.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val gson = Gson()

    private fun createJsonFromTrack(tracks: ArrayList<Track>): String {
        return gson.toJson(tracks)
    }

    private fun createTracksFromJson(tracks: String?): ArrayList<Track> {
        return if (tracks != null) {
            val list = gson.fromJson(tracks, Array<Track>::class.java).toList()
            ArrayList(list)
        } else {
            ArrayList()
        }
    }

    fun addToHistory(track: Track) {




        val historyTrackList = getHistoryTrackList()
        val existingTrackIndex = historyTrackList.indexOfFirst { it.trackId == track.trackId }

        if (existingTrackIndex != -1) {

            historyTrackList.removeAt(existingTrackIndex)
        } else if (historyTrackList.size >= 10) {

            historyTrackList.removeLast()
        }

        historyTrackList.add(0,track)
        sharedPrefs.edit().remove(TRACK_HISTORY_KEY).apply()
        sharedPrefs.edit()
            .putString(TRACK_HISTORY_KEY, createJsonFromTrack(historyTrackList))
            .apply()

    }

    fun getHistoryTrackList(): ArrayList<Track> {
        val tracksJson = sharedPrefs.getString(TRACK_HISTORY_KEY, null)
        return if (tracksJson != null) {
            createTracksFromJson(tracksJson)
        } else {
            ArrayList()
        }
    }
}