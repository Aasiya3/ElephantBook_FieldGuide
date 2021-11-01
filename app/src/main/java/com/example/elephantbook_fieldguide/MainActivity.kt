package com.example.elephantbook_fieldguide

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.time.OffsetDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elephantpage)

        // https://github.com/osmdroid/osmdroid/wiki/Important-notes-on-using-osmdroid-in-your-app
        //5.6 and newer
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        // TESTING CODE
        val simpleImageView = findViewById<ImageView>(R.id.elephantPicture)
        val dbW = DatabaseWrapper(applicationContext)
        //dbW.updateDatabase {
        simpleImageView.setImageDrawable(dbW.getElephantPfp(5))
        println(dbW.getElephantsByNamePrefix("F"))
        println(dbW.getLatestLocation(5))
        //}

        setupMap(findViewById<MapView>(R.id.mapView), dbW, 5)

        // TESTING CODE

    }

    private fun addMarker(mapView: MapView, location: Location) {
        val elephantPoint = GeoPoint(location.latitude, location.longitude)
        val elephantMarker = Marker(mapView)
        elephantMarker.position = elephantPoint
        elephantMarker.snippet = location.dateTime.toString()
        mapView.overlays.add(elephantMarker)
    }

    private fun setupMap(mapView: MapView, dbW: DatabaseWrapper, id: Int) {
        val elephantLocations = dbW.getLocationById(id).sortedBy { it.dateTime }
        // "If MapView is not provided, infowindow popup will not function unless you set it yourself."
        // This is desired behavior. Empty infowindows are not useful
        val polyline = Polyline()
        // I realize we're iterating twice and constructing GeoPoints twice, but I'm preferring
        // cleaner code to performance at this point
        // We do the line before the markers cause otherwise we draw over markers :(
        for (location in elephantLocations) {
            polyline.addPoint(GeoPoint(location.latitude, location.longitude))
        }
        mapView.overlays.add(polyline)

        for (location in elephantLocations) {
            addMarker(mapView, location)
        }

        // If we have any locations, center on that. Otherwise center on default location
        val recentPoint =
            if (elephantLocations.isNotEmpty()) {
                val recentLocation = elephantLocations.last()
                GeoPoint(recentLocation.latitude, recentLocation.longitude)
            } else {
                GeoPoint(-1.49, 35.143889)
            }
        mapView.controller.setZoom(11.0)
        mapView.controller.animateTo(recentPoint)
    }
}