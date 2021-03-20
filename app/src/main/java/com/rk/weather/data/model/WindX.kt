package com.rk.weather.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WindX(
    val deg: Double,
    val speed: Double
) : Parcelable {
    fun getSpeed(): String {
        return "Wind " + speed.toString().substringBefore(".") + "km/hr"
    }
}