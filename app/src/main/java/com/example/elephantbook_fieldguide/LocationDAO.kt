package com.example.elephantbook_fieldguide

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDAO {
    @Insert
    fun insertAll(locations: List<Location>)

    @Query("SELECT * FROM Location WHERE elephantId = :id")
    fun getById(id: Int): List<Location>

    @Query("SELECT * FROM Location")
    fun getAll(): List<Location>

    @Query("SELECT * FROM Location WHERE elephantId = :id ORDER BY dateTime DESC LIMIT 1")
    fun getLatest(id: Int): Location?

    @Query("DELETE FROM Location")
    fun clear()
}