package com.example.myplaylistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.myplaylistmaker.db.entity.PlaylistEntity
import com.example.myplaylistmaker.db.entity.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT id FROM playlist_table")
    suspend fun getPlaylistsId(): List<Int>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity?

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: String) {
        val playlist = getPlaylistById(playlistId)
        playlist?.let {
            val updatedTrackIds = it.tracksIds.split(",").toMutableList()
            if (!updatedTrackIds.contains(trackId)) {
                updatedTrackIds.add(trackId)
                val updatedTrackIdsString = updatedTrackIds.joinToString(",")
                val updatedPlaylist = it.copy(tracksIds = updatedTrackIdsString, quantityTracks = it.quantityTracks + 1)
                updatePlaylist(updatedPlaylist)
            }
        }
    }
}