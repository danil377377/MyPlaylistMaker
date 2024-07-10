package com.example.myplaylistmaker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistEntityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)


}