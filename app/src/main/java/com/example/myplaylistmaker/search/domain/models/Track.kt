package com.example.myplaylistmaker.search.domain.models

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val coverArtWork: String?,
    var isFavorite: Boolean = false
):Serializable

