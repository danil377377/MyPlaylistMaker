package com.example.myplaylistmaker.media.presentation

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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
private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    fun onImageSelected(uri: Uri?) {
        _imageUri.value = uri
        uri?.let { saveImageToPrivateStorage(it) }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }


    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
    }

    fun shouldShowConfirmDialog(): Boolean {
        return !(_name.value.isNullOrEmpty() && _description.value.isNullOrEmpty() && _imageUri ==MutableLiveData<Uri?>())
    }
}