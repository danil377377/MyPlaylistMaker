package com.example.myplaylistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylistmaker.db.dao.TrackDao
import com.example.myplaylistmaker.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
}