package com.example.postindustriaandroid.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.postindustriaandroid.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var latLng: LatLng
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        search_map_btn.setOnClickListener {
            toMapSearchActivity()
        }
    }

    private fun toMapSearchActivity() {
        val intent = Intent(this, MapSearchActivity::class.java)
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        checkPermission()
        map = googleMap
        val zoomLevel = 15f
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            val latitude = it.latitude
            val longitude = it.longitude
            latLng = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
            map.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun checkPermission() {
        applicationContext?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val array = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                requestPermissions(array, REQUEST_LOCATION_PERMISSION)
            }
        }
    }
}
