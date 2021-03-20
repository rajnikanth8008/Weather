package com.rk.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.data.db.entity.ForecastEntity

@Dao
interface ForecastDao {

    @Transaction
    fun deleteAndInsert(forecastEntity: ForecastEntity) {
        deleteCurrentWeather()
        insertCurrentWeather(forecastEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(forecastEntity: ForecastEntity)

    @Query("DELETE FROM Forecast")
    fun deleteCurrentWeather()

    @Query("SELECT * FROM Forecast")
    fun getForecastData(): LiveData<ForecastEntity>
}