package com.example.myplaylistmaker.search.data.sharedprefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.myplaylistmaker.search.domain.api.SharedPrefs
import com.example.myplaylistmaker.search.domain.models.Track
import com.google.gson.Gson

class SharedPrefsImpl(private val context: Context):SharedPrefs {
    companion object {
        const val SHARED_PREFERENCES = "sgared_preferences"
        const val TRACK_HISTORY_KEY = "key_for_track_history"
    }
   val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    private val gson = Gson()
     fun createJsonFromTrack(tracks: ArrayList<Track>): String {
        return gson.toJson(tracks)
    }

    fun createTracksFromJson(tracks: String?): ArrayList<Track> {
        return if (tracks != null) {
            val list = gson.fromJson(tracks, Array<Track>::class.java).toList()
            ArrayList(list)
        } else {
            ArrayList()
        }
    }
     override fun addToHistorySharedPrefs(trackList: ArrayList<Track>){
          sharedPrefs.edit().remove(TRACK_HISTORY_KEY).apply()
          sharedPrefs.edit()
              .putString(TRACK_HISTORY_KEY, createJsonFromTrack(trackList))
              .apply()
     }

    override fun getHistory(): ArrayList<Track>{
        return createTracksFromJson(getHistoryFromJson())
    }
      fun getHistoryFromJson():String?{
         return sharedPrefs.getString(TRACK_HISTORY_KEY, null)
     }
   override fun clearHistorySharedPrefs(){
        sharedPrefs.edit().remove(TRACK_HISTORY_KEY).apply()
    }
}