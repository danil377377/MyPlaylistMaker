package com.example.myplaylistmaker.media.data.converters

import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.media.domain.models.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.pathToFile,
            playlist.tracksIds,
            playlist.quantityTracks
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