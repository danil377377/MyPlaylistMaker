package com.example.myplaylistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description : String,
    val pathToFile: String?,
    val tracksIds: String,
    var quantityTracks: Int
)