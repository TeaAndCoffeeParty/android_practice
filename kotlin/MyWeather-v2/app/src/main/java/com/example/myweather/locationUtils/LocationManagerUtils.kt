package com.example.myweather.locationUtils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class LocationManagerUtils(val context: Context, ) {
    private val permissionRequestCode = 1
    interface PermissionCallback {
        fun onPermissionGranted()
        fun onPermissionDenied()
        fun onLocationChanged(location: Location)
    }

    private var permissionCallback: PermissionCallback? = null
    private val locationManager: LocationManager = context.getSystemService(
        Context.LOCATION_SERVICE) as LocationManager
    private val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            permissionCallback?.onLocationChanged(location)
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }

    fun setPermissionCallback(callback: PermissionCallback) {
        permissionCallback = callback
    }
    private fun checkLocationPermissions() : Boolean {
        val fineLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        return (fineLocationPermission  == PackageManager.PERMISSION_GRANTED
                && coarseLocationPermission == PackageManager.PERMISSION_GRANTED)
    }

    fun requestLocationPermission(activity: Activity) {
        if(checkLocationPermissions()) {
            ActivityCompat.requestPermissions(activity, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), permissionRequestCode)
        }
    }

    @SuppressLint("MissingPermission")
    fun requestGPSLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000L,
            1000f,
            locationListener)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (!allPermissionsGranted) {
                permissionCallback?.onPermissionDenied()
            } else {
                permissionCallback?.onPermissionGranted()
            }
        }

    }
}