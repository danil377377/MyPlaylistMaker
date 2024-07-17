package com.example.myplaylistmaker.media.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.myplaylistmaker.media.domain.ImageDecoder
import java.io.File

class ImageDecoderImpl : ImageDecoder {
    override fun decodeImage(file: File?): Bitmap? {
        return file?.let {
            BitmapFactory.decodeFile(it.path)
        }
    }
}