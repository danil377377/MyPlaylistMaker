package com.example.myplaylistmaker.media.presentation

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistViewModel(application: Application, val imageDecoder: ImageDecoder): AndroidViewModel(application)  {
    fun getImageBitmap(playlist: Playlist): Bitmap?{
        return playlist.getImage(imageDecoder)
    }
}