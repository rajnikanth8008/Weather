package com.rk.weather.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Parcelable {

    fun getDescriptionText(): String? {
        return description?.capitalize()
    }
}