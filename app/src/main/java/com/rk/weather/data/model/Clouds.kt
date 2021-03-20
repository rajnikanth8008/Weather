package com.rk.weather.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Clouds(
    val all: Int
) : Parcelable {
    constructor(clouds: Clouds?) : this(
        all = clouds?.all ?: 0
    )
}