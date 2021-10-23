package com.example.elephantbook_fieldguide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.io.ByteArrayOutputStream

class ApiGetter(
    // Don't understand what this is but we need one and only activities can get one AFAIK
    private val ctx: Context,
) {
    // Build the request queue and make the request
    private val queue = Volley.newRequestQueue(ctx)

    // URL to query the API at
    private val apiUrl = "https://localhost/individuals.json"

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
        val elephantArrReq = JsonArrayRequest(
            apiUrl,
            // Call the successCallback with the parsed data, so caller just gets a nice Pair of Lists
            { response -> successCallback(parseApiResponse(response)) },
            { err -> failCallback(err) },
        )

        // Send the request
        queue.add(elephantArrReq)
    }

    private val imageLoader = ImageLoader(queue, object : ImageLoader.ImageCache {
        val cache = LruCache<String, Bitmap>(50)
        override fun getBitmap(url: String?): Bitmap? {
            return cache.get(url)
        }
        override fun putBitmap(url: String?, bitmap: Bitmap?) {
            cache.put(url, bitmap)
        }
    })

    fun downloadImage(url: String, path: String) {
        imageLoader.get(
            url,
            object : ImageLoader.ImageListener {
                override fun onResponse(
                    response: ImageLoader.ImageContainer,
                    isImmediate: Boolean
                ) {
                    if (isImmediate && (response.bitmap == null)) return
                    ctx.openFileOutput(path, Context.MODE_PRIVATE).use {
                        val stream = ByteArrayOutputStream()
                        response.bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        it.write(stream.toByteArray())
                    }
                }

                override fun onErrorResponse(err: VolleyError) {
                    println(err) // TODO Error Handling
                }
            }
        )
    }
}