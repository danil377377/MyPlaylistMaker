package com.example.myplaylistmaker.media.presentation

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.media.domain.ImageDecoder
import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(application: Application,
                            private val playlistInteractor: PlaylistInteractor,
    val imageDecoder: ImageDecoder
): MakePlaylistViewModel(application, playlistInteractor) {
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist
    fun observePlaylist():LiveData<Playlist> = playlist



    fun initPlaylist(playlist:Playlist){
        _playlist.postValue(playlist)
    }
    fun getImageBitmap(playlist: Playlist): Bitmap? {
        return playlist.getImage(imageDecoder)
    }

    fun updatePlaylist(playlistId: Int){
        viewModelScope.launch {
            playlistInteractor.updatePlaylistById(playlistId, name = super.name.value?:"errorr",super.description.value?:"",super.filePath.value)
        }
    }
    override suspend fun saveToDb(){
        imageUri.value?.let { saveImageToPrivateStorage(it) }
    }
}