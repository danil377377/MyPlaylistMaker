package com.example.myplaylistmaker.media.domain


import java.io.File

interface ImageStorage {
    fun saveImage(uri: String, name: String): String?
}