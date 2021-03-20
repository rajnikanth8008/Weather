package com.rk.weather.utills

object Constants {
    object NetworkService {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val API_KEY_VALUE = "751d80f6c314139192ffcb62c107e654"
        const val RATE_LIMITER_TYPE = "data"
        const val API_KEY_QUERY = "appid"
    }

    object Weather {
        const val WEATHER = "weather"
        const val FORECAST = "forecast"
    }

    object Coords {
        const val LAT = "lat"
        const val LON = "lon"
        const val UNITS = "units"
        const val METRIC = "metric"
    }

    object MapBox {
        const val ACCESS_TOKEN =
//            "pk.eyJ1IjoiemFoaWQxNiIsImEiOiJja2UxZ3lpaGE0NHFuMnJtcXc5djcxeGVtIn0.V5lnAKqektnfC1pARBQYUQ"
            "pk.eyJ1IjoicmFqbmlrYW50aDgwMDgiLCJhIjoiY2tscDZpb3c4MTAwZzJ2bjFjMDZiM24ybyJ9.2yRdrfSs7NDa7S66qDZr2A"
    }
}