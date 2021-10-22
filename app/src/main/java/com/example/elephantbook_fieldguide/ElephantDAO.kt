package com.example.elephantbook_fieldguide

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ElephantDAO {
    @Insert
    fun insertAll(elephants: List<Elephant>)

    @Query("SELECT * FROM Elephant WHERE id = :id")
    fun getById(id: Int): Elephant

    @Query("SELECT * FROM Elephant")
    fun getAll(): List<Elephant>
}