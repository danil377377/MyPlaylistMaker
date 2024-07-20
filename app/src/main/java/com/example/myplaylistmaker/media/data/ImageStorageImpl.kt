package com.example.myplaylistmaker.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.myplaylistmaker.media.domain.ImageStorage
import java.io.File
import java.io.FileOutputStream

class ImageStorageImpl(private val context: Context) : ImageStorage {

    override fun saveImage(uri: String, name: String): String? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$name.jpg")
        val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.path
    }

}