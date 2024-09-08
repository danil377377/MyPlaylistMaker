package com.example.myplaylistmaker.media.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.media.domain.db.PlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

open class MakePlaylistViewModel(
    application: Application,
    private val playlistInteractor: PlaylistInteractor,
) : AndroidViewModel(application) {

private val _name = MutableLiveData<String>()
    open val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    open val description: LiveData<String> = _description

    private val _imageUri = MutableLiveData<String?>()
    open val imageUri: LiveData<String?> = _imageUri
    private val _filePath = MutableLiveData<String?>()
    open val filePath:LiveData<String?> = _filePath
    private val playlistsList = MutableLiveData<List<Playlist>>()
    fun  observePlaylists(): LiveData<List<Playlist>> = playlistsList

    var lastPlaylists = emptyList<Playlist>()

    fun onImageSelected(uri: String?) {
        _imageUri.value = uri


    }

    fun saveImageToPrivateStorage(uri: String) {
        _filePath.value= playlistInteractor.saveImageToPrivateStorage(uri, name.value?:"test")
    }


    fun getListOfPlaylists(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{ playlists ->
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
    open suspend fun saveToDb(){

        imageUri.value?.let { saveImageToPrivateStorage(it) }
playlistInteractor.addPlaylist(Playlist(id = 0, name = name.value.toString(), description = description.value.toString(), pathToFile = filePath.value, tracksIds = "", quantityTracks = 0))
       viewModelScope.launch {  playlistInteractor.getPlaylists().collect{ playlistsList->
           lastPlaylists = playlistsList

       }}
    }
}