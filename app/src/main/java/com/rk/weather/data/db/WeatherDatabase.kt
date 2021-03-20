package com.rk.weather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rk.weather.data.db.dao.BookmarkDao
import com.rk.weather.data.db.dao.ForecastDao
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.data.db.entity.ForecastEntity
import com.rk.weather.utills.DataConverter

@Database(
    entities = [
        BookmarkEntity::class,
        ForecastEntity::class
    ],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun forecastDao(): ForecastDao

    companion object {
        private var instance: WeatherDatabase? = null
        fun getDatabase(context: Context): WeatherDatabase? {
            if (instance == null) {
                synchronized(WeatherDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java, "WeatherApp.db"
                    ).build()
                }
            }
            return instance
        }
    }
}