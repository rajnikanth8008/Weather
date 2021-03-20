package com.rk.weather.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sys(
    val sunrise: Int,
    val sunset: Int
) : Parcelable