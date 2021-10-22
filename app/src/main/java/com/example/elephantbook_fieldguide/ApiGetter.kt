package com.example.elephantbook_fieldguide

import android.content.Context
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ApiGetter {
    // URL to query the API at
    private val apiUrl = Secrets().apiUrl

    private fun parseApiResponse(response: JSONArray): Pair<List<Elephant>, List<Location>> {
        // Loop through the list of Elephants in the JSON to create lists of Elephant and Location objects
        val elephants = mutableListOf<Elephant>()
        val locations = mutableListOf<Location>()
        for (i in 0 until response.length()) {
            // Create the Elephant object
            val newElephant = Elephant(response.getJSONObject(i))
            elephants.add(newElephant)

            // Create the Location objects using the Sightings array and the Elephant's id
            val sightingsArr = response.getJSONObject(i).getJSONArray("sightings")
            for (j in 0 until sightingsArr.length()) {
                locations.add(Location(newElephant.id, sightingsArr.getJSONObject(j)))
            }
        }

        // Return the Elephant and Location data as arrays of class objects
        return Pair(elephants, locations)
    }

    fun getElephantData(
        // Don't understand what this is but we need one and only activities can get one AFAIK
        ctx: Context,
        // We call this with the lists of response data
        successCallback: (Pair<List<Elephant>, List<Location>>) -> Unit,
        // This is technically a VolleyException, but no need to nitpick
        failCallback: (Exception) -> Unit
    ) {
        // Build the request queue and make the request
        val queue = Volley.newRequestQueue(ctx)
        val elephantArrReq = JsonArrayRequest(
            apiUrl,
            // Call the successCallback with the parsed data, so caller just gets a nice Pair of Lists
            { response -> successCallback(parseApiResponse(response)) },
            { err -> failCallback(err) },
        )

        // Send the request
        queue.add(elephantArrReq)
    }
}