package com.example.myplaylistmaker.media.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application, val imageDecoder: ImageDecoder, val playlistInteractor: PlaylistInteractor): AndroidViewModel(application)  {
    fun getImageBitmap(playlist: Playlist): Bitmap?{
        viewModelScope.launch {

                playlistInteractor.getTracksFromPlaylist(playlist.id).collect{
                tracks -> Log.d("tracks", tracks.toString())
            }
        }
        return playlist.getImage(imageDecoder)

    }

}