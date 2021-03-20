package com.rk.weather.data.api

import com.rk.weather.data.model.CurrentWeather
import com.rk.weather.data.model.ForecastWeather
import com.rk.weather.utills.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.Weather.WEATHER)
    fun getCurrentWeather(
        @Query(Constants.Coords.LAT)
        latitude: Double,
        @Query(Constants.Coords.LON)
        longitude: Double,
        @Query("units")
        units: String
    ): Single<CurrentWeather>

    @GET(Constants.Weather.FORECAST)
    fun getForecastWeather(
        @Query(Constants.Coords.LAT)
        lat: Double,
        @Query(Constants.Coords.LON)
        lon: Double,
        @Query(Constants.Coords.UNITS)
        units: String
    ): Single<ForecastWeather>
}