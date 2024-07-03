package com.example.myplaylistmaker.media.domain.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

data class Playlist(
    val id: Int,
    val name: String,
    val description : String,
    val pathToFile: File?,
    val tracksIds: String,
    val quantityTracks: Int
){
    fun getImage(): Bitmap {
        // Создание объекта Bitmap из файла
        return BitmapFactory.decodeFile(pathToFile.toString())

    }
}