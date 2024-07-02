package com.example.myplaylistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylistmaker.db.dao.PlaylistDao
import com.example.myplaylistmaker.db.dao.TrackDao
import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.db.entity.TrackEntity

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao():PlaylistDao
}