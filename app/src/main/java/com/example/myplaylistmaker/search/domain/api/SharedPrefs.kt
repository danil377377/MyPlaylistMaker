package com.example.myplaylistmaker.search.domain.api

import com.example.myplaylistmaker.search.domain.models.Track

interface SharedPrefs {
    fun addToHistorySharedPrefs(trackList: ArrayList<Track>)
    fun clearHistorySharedPrefs()
    suspend fun getHistory(): ArrayList<Track>
}