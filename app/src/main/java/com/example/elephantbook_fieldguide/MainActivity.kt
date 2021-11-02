package com.example.elephantbook_fieldguide

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elephantpage)

        // https://github.com/osmdroid/osmdroid/wiki/Important-notes-on-using-osmdroid-in-your-app
        //5.6 and newer
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        // TESTING CODE
        val elephantID = 5
        val dbW = DatabaseWrapper(applicationContext)
        // TESTING CODE

        findViewById<ImageView>(R.id.elephantPicture).setImageDrawable(dbW.getElephantPfp(elephantID))
        setupMap(findViewById(R.id.mapView), dbW.getLocationsById(elephantID))

        val myElephant = dbW.getElephantById(elephantID)
        findViewById<TextView>(R.id.elephantName).text = myElephant?.name ?: "Elephant not found"
        findViewById<TextView>(R.id.seekCode).text = myElephant?.seek ?: "??T??E????-????X??S???"

        findViewById<TextView>(R.id.lastSeen).text =
            dbW.getLatestLocation(elephantID)?.toString() ?: "No location data available"
    }

    private fun addMarker(mapView: MapView, location: Location) {
        val elephantMarker = Marker(mapView)
        elephantMarker.position = location.toGeoPoint()
        elephantMarker.snippet = location.dateTime.toString()
        mapView.overlays.add(elephantMarker)
    }

    private fun setupMap(
        mapView: MapView,
        locationsUnsorted: List<Location>,
        elephantLocations: List<Location> = locationsUnsorted.sortedBy { it.dateTime }
    ) {
        // "If MapView is not provided, infowindow popup will not function unless you set it yourself."
        // This is desired behavior. Empty infowindows are not useful
        val polyline = Polyline()
        // I realize we're iterating twice and constructing GeoPoints twice, but I'm preferring
        // cleaner code to performance at this point
        // We do the line before the markers cause otherwise we draw over markers :(
        for (location in elephantLocations) {
            polyline.addPoint(location.toGeoPoint())
        }
        mapView.overlays.add(polyline)

        // Add all the markers to the map
        for (location in elephantLocations) {
            addMarker(mapView, location)
        }

        // If we have any locations, center on the most recent. Otherwise center on default location
        val recentPoint =
            if (elephantLocations.isNotEmpty()) {
                // Most recent point is last in the list since we sorted by time
                elephantLocations.last().toGeoPoint()
            } else {
                // This is just someplace nearby the Mara reserve
                GeoPoint(-1.49, 35.143889)
            }
        mapView.controller.setZoom(11.0)
        mapView.controller.animateTo(recentPoint)
    }
}