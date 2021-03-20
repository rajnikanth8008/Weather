package com.rk.weather

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.rk.weather.data.db.WeatherDatabase

class Weather : Application() {
    private val TAG = "Weather"
    override fun onCreate() {
        super.onCreate()
        try {
            AndroidThreeTen.init(this)
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: " + e.message)
        }
    }
}