package com.example.elephantbook_fieldguide

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ApiGetter {
    val apiUrl = "http://localhost:8080"

    private fun parseDate(dateString: String): OffsetDateTime {
        val cleanedDateString = dateString.trim().replace(' ', 'T')
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime.parse(cleanedDateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    fun updateElephantData(): Pair<List<Elephant>, Map<Int, List<Location>>> {
        val newElephants = mutableListOf<Elephant>()
        val newLocations = mutableMapOf<Int, MutableList<Location>>()

        /*
        DO THE ACTUAL API INTERACTION, MOCKED HERE
         */

        /* MOCK */
        newLocations[1] = mutableListOf()
        newLocations[1]!!.add(
            Location(
                parseDate("2021-05-03 13:49:30.698000+00:00"),
                -1.2156474578899041,
                35.126746204831626,
            )
        )

        newElephants.add(
            Elephant(
                1,
                "b70T01E808_-403?X00S00?",
                "FakeElephant",
            )
        )
        /* MOCK */

        return Pair(newElephants, newLocations)
    }
}