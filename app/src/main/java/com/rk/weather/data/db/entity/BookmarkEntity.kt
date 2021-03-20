package com.rk.weather.data.db.entity

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Bookmark", primaryKeys = ["lat", "lon"])
data class BookmarkEntity(
    var placeName: String,
    var lat: Double,
    var lon: Double
) : Parcelable {

    fun getColor(): Int {
        return /*when (dt.let { it?.let { it1 -> getDateTime(it1) } }) {
            DayOfWeek.MONDAY -> Color.parseColor("#28E0AE")
            DayOfWeek.TUESDAY -> Color.parseColor("#FF0090")
            DayOfWeek.WEDNESDAY -> Color.parseColor("#FFAE00")
            DayOfWeek.THURSDAY -> Color.parseColor("#0090FF")
            DayOfWeek.FRIDAY -> Color.parseColor("#DC0000")
            DayOfWeek.SATURDAY -> Color.parseColor("#0051FF")
            DayOfWeek.SUNDAY -> Color.parseColor("#3D28E0")
            else ->*/ Color.parseColor("#28E0AE")
    }
//    }
}