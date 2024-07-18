package com.example.myplaylistmaker.media.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File

class MakePlaylistViewModel(
    application: Application,
    private val makePlaylistInteractor: MakePlaylistInteractor,
) : AndroidViewModel(application) {

private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _imageUri = MutableLiveData<String?>()
    val imageUri: LiveData<String?> = _imageUri
    private val _filePath = MutableLiveData<String?>()
    val filePath:LiveData<String?> = _filePath
    private val playlistsList = MutableLiveData<List<Playlist>>()
    fun  observePlaylists(): LiveData<List<Playlist>> = playlistsList

    var lastPlaylists = emptyList<Playlist>()

    fun onImageSelected(uri: String?) {
        _imageUri.value = uri
        uri?.let { saveImageToPrivateStorage(it) }

    }

    private fun saveImageToPrivateStorage(uri: String) {
        _filePath.value= makePlaylistInteractor.saveImageToPrivateStorage(uri, name.value?:"test")
    }


    fun getListOfPlaylists(){
        viewModelScope.launch {
            makePlaylistInteractor.getPlaylists().collect{playlists ->
                playlistsList.postValue(playlists)
            }
        }
    }


    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
    }

    fun shouldShowConfirmDialog(): Boolean {
        return !(_name.value.isNullOrEmpty() && _description.value.isNullOrEmpty() && _imageUri.value == null)
    }
    suspend fun saveToDb(){
makePlaylistInteractor.addPlaylist(Playlist(id = 0, name = name.value.toString(), description = description.value.toString(), pathToFile = filePath.value, tracksIds = "", quantityTracks = 0))
       viewModelScope.launch {  makePlaylistInteractor.getPlaylists().collect{playlistsList->
           lastPlaylists = playlistsList

       }}
    }
}