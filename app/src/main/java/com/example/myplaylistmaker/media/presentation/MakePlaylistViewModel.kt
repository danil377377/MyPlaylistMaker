package com.example.myplaylistmaker.media.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class MakePlaylistViewModel(
    application: Application,
    val favoritesInteractor: FavoritesInteractor,
) : AndroidViewModel(application) {
//    private val tracks = ArrayList<Track>()
//    private val tracksLiveData = MutableLiveData<ArrayList<Track>>()
//
//    fun observeFavorites(): LiveData<ArrayList<Track>> = tracksLiveData
//    suspend fun getFavoritesTrackList() {
//
//        viewModelScope.launch {
//            favoritesInteractor.getFavoritesTracks().collect { tracks ->
//                val modifiedTracks = tracks.map { track ->
//                    track.copy(isFavorite = true)
//                }
//                tracksLiveData.postValue(ArrayList(modifiedTracks))
//
//            }
//
//        }
//    }
}