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
import java.io.File

class ApiGetter(
    // Don't understand what this is but we need one and only activities can get one AFAIK
    private val ctx: Context,
) {
    // Build the request queue and make the request
    private val queue = Volley.newRequestQueue(ctx)

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

    fun downloadImage(url: String, path: String, then: () -> Unit) {
        // If this file already exists, we're done
        if(File(path).exists()) {
            then()
            return
        }
        // Create the ImageLoader for this URL
        imageLoader.get(
            url,
            object : ImageLoader.ImageListener {
                override fun onResponse(
                    response: ImageLoader.ImageContainer,
                    isImmediate: Boolean
                ) {
                    // If this is immediate and we got no bitmap, this is a cache miss and we ignore it
                    if (isImmediate && (response.bitmap == null)) return
                    // Open the file and write the image to it
                    ctx.openFileOutput(path, Context.MODE_PRIVATE).use {
                        val stream = ByteArrayOutputStream()
                        // This compression works for jpegs also... not sure if that's cool or not
                        response.bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        it.write(stream.toByteArray())
                    }
                    // Do our callback action
                    then()
                }

                // Wish we had some error handling anywhere in this code :(
                override fun onErrorResponse(err: VolleyError) {
                    println(err) // TODO Error Handling
                }
            }
        )
    }
}