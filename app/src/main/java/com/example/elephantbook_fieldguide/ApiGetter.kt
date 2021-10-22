package com.example.elephantbook_fieldguide

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.volley.Response
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import java.security.AccessController.getContext
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ApiGetter {
    val apiUrl = Secrets().apiUrl

    private fun parseDate(dateString: String): OffsetDateTime {
        val cleanedDateString = dateString.trim().replace(' ', 'T')
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime.parse(cleanedDateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    fun updateElephantData(): Pair<List<Elephant>, List<Location>> {
        val elephants = mutableListOf<Elephant>()
        val locations = mutableListOf<Location>()

        /* MOCK */
        locations.add(
            Location(
                parseDate("2021-08-03 13:49:30.698000+00:00"),
                -1.215647,
                35.12674,
                1,
            )
        )
        locations.add(
            Location(
                parseDate("2021-01-01 01:01:01.1+00:00"),
                1.5,
                8.9,
                1,
            )
        )

        elephants.add(
            Elephant(
                1,
                "b70T01E808_-403?X00S00?",
                "FakeElephant",
            )
        )
        elephants.add(
            Elephant(
                2,
                "b__T__E____-____X__S___",
                "Invisible Elephant"
            )
        )
        /* MOCK */
        return Pair(elephants, locations)
    }
}