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

    fun updateDatabase(then: () -> Unit) {
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
        val givenCode = seek.toMutableList()
        val otherCode = otherSeek.toMutableList()

        val equal: MutableList<Int> = arrayListOf()
        for (i in givenCode.indices) {
            if (givenCode[i] == otherCode[i]) {
                equal.add(1)
            } else {
                equal.add(0)
            }
        }

        //Equal or one is wildcard
        var penalty = 0 //wildcard penalty
        var average = 0
        for (i in givenCode.indices) {
            if (equal[i] == 1 || givenCode[i] == '?' || otherCode[i] == '?') {
                average++
                if (otherCode[i] == '?') {
                    penalty++
                }
            }
        }
        average /= givenCode.size
        penalty /= givenCode.size
        return average - penalty
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