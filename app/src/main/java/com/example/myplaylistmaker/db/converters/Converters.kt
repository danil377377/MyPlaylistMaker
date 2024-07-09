package com.example.myplaylistmaker.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class Converters {

    @TypeConverter
    fun fromFile(file: File?): String? {
        return file?.path
    }

    @TypeConverter
    fun toFile(path: String?): File? {
        return path?.let { File(it) }
    }

    @TypeConverter
    fun fromTrackIds(trackIds: List<String>?): String? {
        return Gson().toJson(trackIds)
    }

    @TypeConverter
    fun toTrackIds(trackIdsString: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(trackIdsString, listType)
    }
}