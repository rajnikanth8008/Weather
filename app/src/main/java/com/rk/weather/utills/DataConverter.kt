package com.rk.weather.utills

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.data.model.ListItem
import com.rk.weather.data.model.WeatherX
import java.lang.reflect.Type

object DataConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): List<BookmarkEntity> {
        val listType: Type = object : TypeToken<List<BookmarkEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    @JvmStatic
    fun fromList(list: List<BookmarkEntity>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromListToString(value: String?): List<ListItem> {
        val listType: Type = object : TypeToken<List<ListItem>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    @JvmStatic
    fun fromListToString(list: List<ListItem>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    @JvmStatic
    fun fromWeatherStringToList(value: String?): List<WeatherX> {
        val listType: Type = object : TypeToken<List<WeatherX>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    @JvmStatic
    fun fromWeatherListToString(list: List<WeatherX>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}