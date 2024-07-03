package com.example.myplaylistmaker.media.data.converters

import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(

           name = playlist.name,
            description = playlist.description,
            pathToFile = playlist.pathToFile,
            tracksIds = playlist.tracksIds,
            quantityTracks = playlist.quantityTracks
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.pathToFile,
            playlistEntity.tracksIds,
            playlistEntity.quantityTracks
        )
    }
}