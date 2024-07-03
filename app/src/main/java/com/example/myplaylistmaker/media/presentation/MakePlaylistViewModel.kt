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
import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MakePlaylistViewModel(
    application: Application,
    private val makePlaylistInteractor: MakePlaylistInteractor,
) : AndroidViewModel(application) {

private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri
    private val _filePath = MutableLiveData<File>()
    val filePath:LiveData<File> = _filePath

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
        val file = File(filePath, "$name.jpg")
        _filePath.value=file
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
        return !(_name.value.isNullOrEmpty() && _description.value.isNullOrEmpty() && _imageUri.value == null)
    }
    suspend fun saveToDb(){
makePlaylistInteractor.addPlaylist(Playlist(id = 0, name = name.value.toString(), description = description.value.toString(), pathToFile = filePath.value, tracksIds = "", quantityTracks = 0))
    }
}