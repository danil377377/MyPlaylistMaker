package com.example.myplaylistmaker.db.converters

import androidx.room.TypeConverter
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
}