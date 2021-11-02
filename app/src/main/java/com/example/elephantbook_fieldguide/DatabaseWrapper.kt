package com.example.elephantbook_fieldguide

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

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

    fun updateDatabase(then: () -> Unit) {
        apiGetter.getElephantData(
            { response ->
                // Clear and re-fill the database
                elephantDAO.clear()
                locationDAO.clear()
                elephantDAO.insertAll(response.first)
                locationDAO.insertAll(response.second)
                // Counts how many more PFPs we have to download
                // When this hits 0, we're good to move on
                val pfpCounter = AtomicInteger(response.first.size)
                for (elephant in response.first) {
                    // Flatten file structure
                    val pfpFile = elephant.pfp.replace('/', '_')
                    // Download the image
                    apiGetter.downloadImage(elephant.pfp, pfpFile)
                    {
                        // Log a download failure if we didn't get the image
                        if (!it) Log.i("updateDatabase", "Could not get ${elephant.pfp}")
                        // If the pfpCounter is 0, we were the last downloader
                        if (pfpCounter.decrementAndGet() == 0) {
                            // Caller callback
                            then()
                        }
                    }
                }
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

    fun getElephantPfp(id: Int): Drawable? {
        val pfp = getElephantById(id).pfp.replace('/', '_')
        return try {
            Drawable.createFromStream(ctx.openFileInput(pfp), pfp)
        } catch (e: FileNotFoundException) {
            null
        }
    }
    
    fun getElephantsByNamePrefix(prefix: String): List<Elephant> {
        return elephantDAO.getByNamePrefix(prefix).sortedBy { it.name }
    }

    fun getAllLocations(): List<Location> {
        return locationDAO.getAll()
    }

    fun getLocationsById(id: Int): List<Location> {
        return locationDAO.getById(id)
    }

    fun getLatestLocation(id: Int): Location? {
        return locationDAO.getLatest(id)
    }

}