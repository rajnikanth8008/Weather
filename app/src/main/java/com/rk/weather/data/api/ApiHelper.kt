package com.rk.weather.data.api

import com.rk.weather.utills.Constants

class ApiHelper(private val apiService: ApiService) {

    fun getCurrentWeather(latitude: String, longitude: String) = apiService.getCurrentWeather(latitude.toDouble(), longitude.toDouble(), Constants.Coords.METRIC)

    fun getForecastWeather(latitude: String, longitude: String) = apiService.getForecastWeather(latitude.toDouble(), longitude.toDouble(), Constants.Coords.METRIC)
}