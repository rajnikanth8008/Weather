package com.rk.weather.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoordX(
    val lat: Double,
    val lon: Double
) : Parcelable