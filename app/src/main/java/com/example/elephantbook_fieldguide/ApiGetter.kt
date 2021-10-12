package com.example.elephantbook_fieldguide

import android.app.DownloadManager
import android.content.Context
import android.location.GnssAntennaInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.volley.Response
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.security.AccessController.getContext
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ApiGetter {
    private val apiUrl = "https://localhost/individuals.json"

    private fun parseDate(dateString: String): OffsetDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private fun parseApiResponse(response: JSONArray): MutableList<Elephant> {
        val elephants = mutableListOf<Elephant>()
        for (i in 0 until response.length()) {
            elephants.add(Elephant(response.getJSONObject(i)))
        }

        return elephants
    }

    fun updateElephantData(ctx: Context): Pair<List<Elephant>, List<Location>> {
        val elephants: MutableList<Elephant>
        val locations = mutableListOf<Location>()

        // https://stackoverflow.com/questions/16904741/can-i-do-a-synchronous-request-with-volley
        val future = RequestFuture.newFuture<JSONArray>()

        val queue = Volley.newRequestQueue(ctx)
        JsonArrayRequest(apiUrl, future, future)

        elephants = try {
            parseApiResponse(future.get())
        } catch (e: Exception) {
            // TODO Error handling
            println(e.message)
            println(e.stackTrace)
            mutableListOf<Elephant>()
        }

        /* MOCK */
        locations.add(
            Location(
                parseDate("2021-08-03T13:49:30.698000+00:00"),
                -1.215647,
                35.12674,
                1,
            )
        )
        locations.add(
            Location(
                parseDate("2021-01-01T01:01:01.1+00:00"),
                1.5,
                8.9,
                1,
            )
        )
        /* MOCK */
        return Pair(elephants, locations)
    }
}