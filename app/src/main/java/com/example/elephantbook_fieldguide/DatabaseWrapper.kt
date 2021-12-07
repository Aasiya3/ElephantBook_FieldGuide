package com.example.elephantbook_fieldguide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.room.Room
import java.io.FileNotFoundException
import java.util.concurrent.atomic.AtomicInteger

class DatabaseWrapper private constructor(
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var singleton: DatabaseWrapper // This complains but we ignore it ;)
        fun create(ctx: Context): DatabaseWrapper {
            if (!::singleton.isInitialized) singleton = DatabaseWrapper(ctx)
            return singleton
        }
    }

    fun updateDatabase(then: (Boolean) -> Unit) {
        apiGetter.getElephantData(
            { (newElephants, newLocations) ->
                // Clear and re-fill the database
                elephantDAO.clear()
                locationDAO.clear()
                elephantDAO.insertAll(newElephants)
                locationDAO.insertAll(newLocations)
                // Counts how many more PFPs we have to download
                // When this hits 0, we're good to move on
                val pfpCounter = AtomicInteger(newElephants.size)
                for (elephant in newElephants) {
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
                            then(true)
                        }
                    }
                }
            },
            { err ->
                Log.w(
                    "DatabaseWrapper",
                    "Failed to load elephant data with error $err"
                )
                then(false)
            }
        )
    }

    fun getAllElephants(): List<Elephant> {
        return elephantDAO.getAll()
    }

    fun getElephantById(id: Int): Elephant? {
        return elephantDAO.getById(id)
    }

    fun getElephantPfp(id: Int): Drawable? {
        val pfp = getElephantById(id)?.pfp?.replace('/', '_') ?: return null
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

    private fun seekDistance(seek: String, otherSeek: String): Int {
        // This looks tricky but seems as easy as any other way to do this
        // Starting from 0, add 1 to our count any time we hit a ? or a
        // mismatch (we handle the case where otherSeek[index] == '?'
        // implicitly, cause either it matches seek[index] and
        // seek[index] == '?', or it doesn't match seek[index]
        return seek.indices.fold(0) { accumulator: Int, index: Int ->
            accumulator + if (seek[index] == '?' || seek[index] != otherSeek[index]) 1 else 0
        }
    }

    fun getElephantsBySeek(seek: String): List<Elephant> {
        return elephantDAO.getAll().sortedBy { elephant ->
            seekDistance(seek, elephant.seek)
        }
    }

    fun getLatestLocation(id: Int): Location? {
        return locationDAO.getLatest(id)
    }

}