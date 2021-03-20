package com.rk.weather.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import com.rk.weather.data.model.City
import com.rk.weather.data.model.ListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Forecast", primaryKeys = ["lat", "lon"])
class ForecastEntity(
    val cityCountry: String,
    val CityName: String,
    val list: List<ListItem>,
    var lat: Double,
    var lon: Double
) : Parcelable