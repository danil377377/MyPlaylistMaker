package com.example.myplaylistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description : String,
    val pathToFile: String,
    val tracksIds: String,
    val quantityTracks: Int
)