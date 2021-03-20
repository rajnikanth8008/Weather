package com.rk.weather.data.model

data class ForecastWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ListItem>,
    val message: Int
)