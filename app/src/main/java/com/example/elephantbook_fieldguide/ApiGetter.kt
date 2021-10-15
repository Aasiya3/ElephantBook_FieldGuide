package com.example.elephantbook_fieldguide

import android.content.Context
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ApiGetter {
    // URL to query the API at
    private val apiUrl = "https://localhost/individuals.json"

    private fun parseApiResponse(response: JSONArray): Pair<List<Elephant>, List<Location>> {
        // Loop through the list of Elephants in the JSON to create a list of Elephant objects
        val elephants = mutableListOf<Elephant>()
        for (i in 0 until response.length()) {
            elephants.add(Elephant(response.getJSONObject(i)))
        }

        val locations = mutableListOf<Location>()
        // For each elephant, associate its sightings with its id and create Location objects
        for (i in 0 until response.length()) {
            val sightingsArr = response.getJSONObject(i).getJSONArray("sightings")
            val id = response.getJSONObject(i).getInt("id")
            for (j in 0 until sightingsArr.length()) {
                locations.add(Location(id, sightingsArr.getJSONObject(j)))
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