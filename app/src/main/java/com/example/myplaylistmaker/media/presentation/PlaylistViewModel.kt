package com.example.myplaylistmaker.media.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistViewModel(
    application: Application,
    val imageDecoder: ImageDecoder,
    val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

    private val tracksList = MutableLiveData<ArrayList<Track>>()
    fun observeTracks(): LiveData<ArrayList<Track>> = tracksList

    private val totalTime = MutableLiveData<Long>()
    fun observeTotalTime(): LiveData<Long> = totalTime

    fun getImageBitmap(playlist: Playlist): Bitmap? {
        return playlist.getImage(imageDecoder)
    }

    fun getTracks(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(playlist.id).collect { tracks ->
                tracksList.postValue(ArrayList(tracks))
            }
        }
    }

    fun getTotalTime(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(playlist.id).collect { tracks ->
                val totalTimeValue = tracks.sumOf { track -> track.trackTimeMillis }
                totalTime.postValue(totalTimeValue)
            }
        }
    }

    fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track.trackId, playlist.id)
        }
    }
}