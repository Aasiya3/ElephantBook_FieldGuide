package com.example.elephantbook_fieldguide

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDAO {
    @Insert
    fun insertAll(locations : List<Location>)

    @Query("SELECT * FROM Location WHERE elephantId = :id")
    fun getById(id : Int) : Location

    @Query("SELECT * FROM Location")
    fun getAll() : List<Location>
}