package com.example.elephantbook_fieldguide

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Elephant::class, Location::class], version = 1)
@TypeConverters(Converters::class)
abstract class ElephantBookDatabase : RoomDatabase() {
    abstract fun elephantDAO(): ElephantDAO
    abstract fun locationDAO(): LocationDAO
}