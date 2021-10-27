package com.example.elephantbook_fieldguide

import android.content.Context
import androidx.room.Room

class DatabaseWrapper(
    private val ctx: Context,
    elephantBookDatabase: ElephantBookDatabase = Room.databaseBuilder(
        ctx,
        ElephantBookDatabase::class.java,
        "elephantBook"
    )
        .allowMainThreadQueries() // TODO DELETE ME
        .build(),
) {
    private val elephantDAO: ElephantDAO = elephantBookDatabase.elephantDAO()
    private val locationDAO: LocationDAO = elephantBookDatabase.locationDAO()
    private val apiGetter: ApiGetter = ApiGetter(ctx)

    fun updateDatabase() {
        apiGetter.getElephantData(
            { response ->
                elephantDAO.clear()
                locationDAO.clear()
                elephantDAO.insertAll(response.first)
                locationDAO.insertAll(response.second)
            },
            { err ->
                println("BRSAKAI_ERR")
                println(err) // TODO Error handling
            }
        )
    }

    fun getAllElephants(): List<Elephant> {
        return elephantDAO.getAll()
    }

    fun getElephantById(id: Int): Elephant {
        return elephantDAO.getById(id)
    }

    fun getElephantsByNamePrefix(prefix: String): List<Elephant> {
        return elephantDAO.getByNamePrefix(prefix).sortedBy { it.name }
    }

    fun getAllLocations(): List<Location> {
        return locationDAO.getAll()
    }

    fun getLocationById(id: Int): List<Location> {
        return locationDAO.getById(id)
    }

    fun getLatestLocation(id: Int): Location? {
        return locationDAO.getLatest(id)
    }

}