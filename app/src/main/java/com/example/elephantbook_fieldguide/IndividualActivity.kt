package com.example.elephantbook_fieldguide

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class IndividualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elephantpage)
        // https://github.com/osmdroid/osmdroid/wiki/Important-notes-on-using-osmdroid-in-your-app
        //5.6 and newer
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        val databaseWrapper = DatabaseWrapper.create(applicationContext)

        val elephantID = intent.getIntExtra("elephantId", -1)

        findViewById<ImageView>(R.id.elephantPicture).setImageDrawable(
            databaseWrapper.getElephantPfp(elephantID)
                ?: AppCompatResources.getDrawable(
                    applicationContext,
                    android.R.drawable.ic_menu_gallery
                )
        )

        setupMap(findViewById(R.id.mapView), databaseWrapper.getLocationsById(elephantID))

        val myElephant = databaseWrapper.getElephantById(elephantID)
        findViewById<TextView>(R.id.elephantName).text = myElephant?.name ?: "Elephant not found"
        findViewById<TextView>(R.id.seekCode).text = myElephant?.seek ?: "???T??E????-????X??S???"

        findViewById<TextView>(R.id.lastSeen).text =
            databaseWrapper.getLatestLocation(elephantID)?.toString()
                ?: "No location data available"
    }

    private fun addMarker(mapView: MapView, location: Location) {
        mapView.overlays.add(
            Marker(mapView).apply {
                position = location.toGeoPoint()
                snippet = location.dateTime.toString()
            }
        )
    }

    private fun addLocationToMap(mapView: MapView) {
        val prov = GpsMyLocationProvider(applicationContext)
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val locationOverlay = MyLocationNewOverlay(prov, mapView)
        locationOverlay.enableMyLocation()
        mapView.overlayManager.add(locationOverlay)
    }

    private fun tryAddLocationToMap(mapView: MapView) {
        // If we already have permission to access the location, add the user's location to the map
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return addLocationToMap(mapView)
        }

        // If we didn't already have access to the location, we need to request it
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Add the marker to the map
                addLocationToMap(mapView)
            } else {
                // Permission not granted. Don't add the marker
                println("No location granted :(")
            }
        }.launch(
            // The permission we're actually requesting
            Manifest.permission.ACCESS_FINE_LOCATION
        )
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
        mapView.controller.apply {
            setZoom(11.0)
            animateTo(recentPoint)
        }

        // Try to add the user's location to the map
        tryAddLocationToMap(mapView)
    }
}