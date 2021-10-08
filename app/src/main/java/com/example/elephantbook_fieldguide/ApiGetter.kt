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
    val apiUrl = "http://192.168.5.11:8000/individuals.json"

    private fun parseDate(dateString: String): OffsetDateTime {
        val cleanedDateString = dateString.trim().replace(' ', 'T')
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime.parse(cleanedDateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    fun updateElephantData(ctx : Context): Pair<List<Elephant>, Map<Int, List<Location>>> {
        val elephants = mutableListOf<Elephant>()
        val locations = mutableMapOf<Int, MutableList<Location>>()

        //https://developer.android.com/training/volley/request#kotlin
        val queue = Volley.newRequestQueue(ctx)
        val individualsJsonReq = JsonArrayRequest(apiUrl,
            { response -> println("BRSAKAI$response") },
            { error -> println("BRSAKAI_ERR$error") })
        queue.add(individualsJsonReq)


        /* MOCK
        locations[1] = mutableListOf()
        locations[1]!!.add(
            Location(
                parseDate("2021-05-03 13:49:30.698000+00:00"),
                -1.2156474578899041,
                35.126746204831626,
            )
        )

        elephants.add(
            Elephant(
                1,
                "b70T01E808_-403?X00S00?",
                "FakeElephant",
            )
        )
        /* MOCK */
        */
        return Pair(elephants, locations)
    }
}