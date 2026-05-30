package com.fel.qrswap.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onResult: (Double, Double) -> Unit
) {

    val client = LocationServices.getFusedLocationProviderClient(context)

    client.lastLocation.addOnSuccessListener { location ->

        if (location != null) {
            onResult(location.latitude, location.longitude)
        } else {

            // Jeżeli nie zadziała - domyślnie Warszawa
            onResult(52.23, 21.01)
        }
    }
}