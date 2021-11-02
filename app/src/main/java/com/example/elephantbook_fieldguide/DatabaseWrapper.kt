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

    fun getAllLocations(): List<Location> {
        return locationDAO.getAll()
    }

    fun getLocationById(id: Int): List<Location> {
        return locationDAO.getById(id)
    }

    private fun seekDistance(seek: String, otherSeek: String): Int{
        val givenCode = seek.toMutableList()
        val otherCode = otherSeek.toMutableList()

        val equal : MutableList<Int> = arrayListOf()
        for(i in givenCode.indices){
            if(givenCode[i] == otherCode[i]){
                equal.add(1)
            }else{
                equal.add(0)
            }
        }

        //Equal or one is wildcard
        var penalty = 0 //wildcard penalty
        var average = 0
        for(i in givenCode.indices){
            if(equal[i] == 1 || givenCode[i] == '?' || otherCode[i] == '?'){
                average++
                if(otherCode[i] =='?'){
                    penalty++
                }
            }
        }
        average /= givenCode.size
        penalty /= givenCode.size
        return average - penalty
    }

    fun getElephantsBySeek(seek: String): List<Elephant> {
        val Elephants = elephantDAO.getAll()
        val sortedElephants = Elephants.sortedBy {
            Elephant -> seekDistance(seek, Elephant.seek)
        }
        return sortedElephants
    }

}