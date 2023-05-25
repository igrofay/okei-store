package com.okei.store.feature.ordering.model

import android.content.Context
import android.icu.text.DecimalFormat
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.*

class LocationClient @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val latitudePoint = 51.765334
    private val longitudePoint = 55.124111
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun subscribeToDistanceChange(onDictationChange: (Double) -> Unit){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Проверка наличия полученной позиции
                if (location != null) {
                    val latitudeClient = location.latitude
                    val longitudeClient = location.longitude
                    onDictationChange(
                        calculateDistance(
                            latitudeClient, longitudeClient,
                            latitudePoint, longitudePoint
                        )
                    )
                } else {
                    // Невозможно получить текущую позицию
                }
            }
            .addOnFailureListener { exception: Exception ->
                // Обработка ошибки при получении позиции
            }
    }
    companion object{
        private const val R = 6371 // Радиус Земли в километрах
        private fun calculateDistance(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double,
        ): Double {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = (sin(dLat / 2) * sin(dLat / 2)
                    + (cos(Math.toRadians(lat1))
                    * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                    * sin(dLon / 2)))
            val c = 2 * asin(sqrt(a))
            val valueResult: Double = R * c
            return valueResult
        }
    }

}