package com.example.myplaylistmaker.media.domain.models

import java.io.File

data class Playlist(
    val id: Int,
    val name: String,
    val description : String,
    val pathToFile: File,
    val tracksIds: String,
    val quantityTracks: Int
)