package com.example.myplaylistmaker.media.domain.impl

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.myplaylistmaker.media.domain.db.MakePlaylistInteractor
import com.example.myplaylistmaker.media.domain.db.MakePlaylistRepository
import com.example.myplaylistmaker.media.domain.models.Playlist
import com.example.myplaylistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream

class MakePlaylistInteractorImpl(private val makePlaylistRepository: MakePlaylistRepository, val context: Context):
    MakePlaylistInteractor {
     override fun saveImageToPrivateStorage(uri: Uri, name: String): File {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$name.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file
    }


    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return  makePlaylistRepository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        makePlaylistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        makePlaylistRepository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track) {
        makePlaylistRepository.addTrackToPlaylist(playlistId, track)
    }


}