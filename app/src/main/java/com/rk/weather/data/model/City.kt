package com.rk.weather.data.model


import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val coord: CoordX,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
): Parcelable {

    fun getCityAndCountry(): String {
        return if (country.equals(""))
            "$name"
        else
            "$name, $country"
    }
}