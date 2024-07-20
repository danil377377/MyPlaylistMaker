package com.example.myplaylistmaker.media.data.converters

import com.example.myplaylistmaker.db.entity.TrackEntity
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.search.data.dto.TrackDto
import com.example.myplaylistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId.toString(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.getCoverArtwork(),
            "trackFile"
        )
    }


    fun map(track: TrackEntity): Track {
        return Track(
            track.id.toInt(),
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.coverArtWork
        )
    }


}