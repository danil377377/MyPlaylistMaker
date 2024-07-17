package com.example.myplaylistmaker.media.domain

import android.net.Uri
import java.io.File

interface ImageStorage {
    fun saveImage(uri: Uri, name: String): File
}