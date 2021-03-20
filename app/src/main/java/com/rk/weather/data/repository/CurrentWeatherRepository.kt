package com.rk.weather.data.repository

import com.rk.weather.data.api.ApiHelper
import com.rk.weather.data.model.CurrentWeather
import com.rk.weather.data.model.ForecastWeather
import io.reactivex.Single

class CurrentWeatherRepository(private val apiHelper: ApiHelper) {

    fun getCurrentWeather(latitude: String, longitude: String): Single<CurrentWeather> =
        apiHelper.getCurrentWeather(latitude, longitude)

    fun getForecastWeather(latitude: String, longitude: String): Single<ForecastWeather> =
        apiHelper.getForecastWeather(latitude, longitude)
}