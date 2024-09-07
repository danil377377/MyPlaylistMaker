package com.example.myplaylistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.db.entity.TrackEntity
import com.example.myplaylistmaker.db.entity.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackInPlaylistEntityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table")
    fun getTracks(): Flow<List<TrackEntity>>
    @Query("DELETE FROM track_in_playlist_table WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Int)

}