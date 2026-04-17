package com.techiteasy.campusatlas.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

/**
 * A custom GPS provider that intercepts Google Play Services Fused Location
 * and feeds it directly into OpenStreetMap for instant, hyper-accurate tracking.
 */
class GoogleFusedLocationProvider(context: Context) : IMyLocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient = 
        LocationServices.getFusedLocationProviderClient(context)
        
    private var myLocationConsumer: IMyLocationConsumer? = null
    private var lastKnownLocation: Location? = null

    // Configure the tracking for highest possible precision and fastest possible interval (matched to Google Maps default)
    private val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .setMinUpdateIntervalMillis(500)
        .setMinUpdateDistanceMeters(0f)
        .build()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR)

    private var currentAzimuth = 0f
    private val alpha = 0.1f // EMA factor for smoothing: lower = smoother, higher = more responsive

    private val sensorEventListener = object : android.hardware.SensorEventListener {
        override fun onSensorChanged(event: android.hardware.SensorEvent) {
            if (event.sensor.type == android.hardware.Sensor.TYPE_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(9)
                android.hardware.SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                
                // For a map app, we want the heading based on where the top of the phone points,
                // so we don't need to remap coordinate systems like an AR app.
                val orientation = FloatArray(3)
                android.hardware.SensorManager.getOrientation(rotationMatrix, orientation)
                
                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                azimuth = (azimuth + 360f) % 360f
                
                // Exponential Moving Average to smooth out hardware jitters
                var delta = azimuth - currentAzimuth
                
                // Handle the 0/360 wrap-around smoothly
                if (delta > 180) delta -= 360
                if (delta < -180) delta += 360
                
                // Update only if difference is noticeable (> 0.2 degrees) to save CPU
                if (kotlin.math.abs(delta) > 0.2f) {
                    currentAzimuth = (currentAzimuth + alpha * delta).toFloat()
                    currentAzimuth = (currentAzimuth + 360f) % 360f
                    
                    lastKnownLocation?.let { loc ->
                        loc.bearing = currentAzimuth
                        myLocationConsumer?.onLocationChanged(loc, this@GoogleFusedLocationProvider)
                    }
                }
            }
        }
        override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {}
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return
            location.bearing = currentAzimuth // Apply the hardware compass bearing to the GPS packet
            lastKnownLocation = location
            myLocationConsumer?.onLocationChanged(location, this@GoogleFusedLocationProvider)
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer): Boolean {
        this.myLocationConsumer = myLocationConsumer
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        // Start physical hardware compass using the new fused Rotation Vector
        rotationSensor?.let { sensorManager.registerListener(sensorEventListener, it, android.hardware.SensorManager.SENSOR_DELAY_GAME) }
        return true
    }

    override fun stopLocationProvider() {
        myLocationConsumer = null
        fusedLocationClient.removeLocationUpdates(locationCallback)
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun getLastKnownLocation(): Location? {
        return lastKnownLocation
    }

    override fun destroy() {
        stopLocationProvider()
    }
}
