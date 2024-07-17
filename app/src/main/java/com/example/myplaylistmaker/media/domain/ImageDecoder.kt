package com.example.myplaylistmaker.media.domain

import android.graphics.Bitmap
import java.io.File

interface ImageDecoder {
    fun decodeImage(file: File?): Bitmap?
}