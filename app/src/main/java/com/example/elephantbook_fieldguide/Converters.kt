package com.example.elephantbook_fieldguide

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

// https://developer.android.com/training/data-storage/room/referencing-data
class Converters {
    @TypeConverter
    fun fromOffsetDateTime(dateTime: OffsetDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    @RequiresApi(Build.VERSION_CODES.O)
    fun toOffsetDateTime(dateTime: String) : OffsetDateTime {
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}