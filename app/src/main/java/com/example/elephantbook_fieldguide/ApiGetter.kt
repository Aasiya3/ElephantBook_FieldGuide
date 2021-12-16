package com.example.elephantbook_fieldguide

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File

class ApiGetter(
    // Don't understand what this is but we need one and only activities can get one AFAIK
    private val ctx: Context,
) {
    // Build the request queue and make the request
    private val queue = Volley.newRequestQueue(ctx)

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
        // We call this with the lists of response data
        successCallback: (Pair<List<Elephant>, List<Location>>) -> Unit,
        // This is technically a VolleyException, but no need to nitpick
        failCallback: (Exception) -> Unit
    ) {
        // Send a request to get the JSON data
        queue.add(
            object : JsonArrayRequest(
                // Request is sent to the API URL
                Secrets.apiUrl,
                // Call the successCallback with the parsed data, so caller just gets a nice Pair of Lists
                { response -> successCallback(parseApiResponse(response)) },
                { err -> failCallback(err) },
            ) {
                // If this is modified, also modify downloadImage!
                // https://www.baeldung.com/kotlin/anonymous-inner-classes
                // Ain't she neat?
                override fun getHeaders() = Secrets.getApiAuthHeaders()
            }
        )
    }

    fun downloadImage(url: String, path: String, then: (Boolean) -> Unit) {
        // If this file already exists, we're done
        if (File(ctx.filesDir, path).exists()) {
            then(true)
            return
        }

        // Send the ImageRequest for this URL
        queue.add(
            object : ImageRequest(
                Secrets.imageUrl + url,
                { response -> // Open the file and write the image to it
                    ctx.openFileOutput(path, Context.MODE_PRIVATE).use {
                        val stream = ByteArrayOutputStream()
                        // This compression works for jpegs also... not sure if that's cool or not
                        response.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        it.write(stream.toByteArray())
                    }
                    then(true)
                },
                0,
                0,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                { err ->
                    Log.d(
                        "ApiGetter",
                        "Failed to load image with error $err"
                    )
                    then(false)
                }
            ) {
                // Copy pasted from getElephantData. Don't see a way around this, but if we
                // change the other one, please also change this one!
                override fun getHeaders() = Secrets.getApiAuthHeaders()
            }
        )
    }
}