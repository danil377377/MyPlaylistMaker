package com.example.myplaylistmaker.media.domain.models

import android.graphics.Bitmap
import com.example.myplaylistmaker.media.domain.ImageDecoder
import java.io.File

data class Playlist(
    val id: Int,
    val name: String,
    val description : String,
    val pathToFile: String?,
    val tracksIds: String,
    val quantityTracks: Int
){
    fun getImage(imageDecoder: ImageDecoder): Bitmap? {
        return imageDecoder.decodeImage(pathToFile)
    }
}