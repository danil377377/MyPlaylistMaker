package com.example.myplaylistmaker.media.domain.models

data class Playlist(
    val id: Int,
    val name: String,
    val description : String,
    val pathToFile: String,
    val tracksIds: String,
    val quantityTracks: Int
)